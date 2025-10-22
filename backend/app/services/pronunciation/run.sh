#!/bin/bash

# This script must be executed from gop home
cd $KALDI_HOME/egs/gop_speechocean762/s5 || exit 1;

# Number of parallel jobs (must be one because we will use one single file per run)
nj=1

# Load the cmd.sh and path.sh scripts
. ./cmd.sh
. ./path.sh
. parse_options.sh

# This recipe depends on the model trained in the librispeech recipe.
librispeech_eg=$KALDI_HOME/egs/librispeech/s5
model=$librispeech_eg/exp/nnet3_cleaned/tdnn_sp
ivector_extractor=$librispeech_eg/exp/nnet3_cleaned/extractor
lang=$librispeech_eg/data/lang
phones=$model/phones.txt

# Input arguments
text_file=$1
wav_file=$2
text_phone=$3
input_dir=$4

# Create data directory
rm -rf $input_dir
local/create_struct.sh $text_file $wav_file $input_dir conf/mfcc_hires.conf > /dev/null || exit 1;

# Create output temp directories
output_dir=$(mktemp -d)

for d in $model $ivector_extractor $lang; do
  [ ! -d $d ] && echo "$0: no such path $d" >&2 && exit 1;
done

# Validate data directory
utils/validate_data_dir.sh --no-feats $input_dir > /dev/null || exit 1;

# Create high-resolution MFCC features
steps/make_mfcc.sh --nj $nj --mfcc-config conf/mfcc_hires.conf --cmd "$cmd" $input_dir > /dev/null || exit 1;
steps/compute_cmvn_stats.sh $input_dir > /dev/null || exit 1;
utils/fix_data_dir.sh $input_dir > /dev/null || exit 1;

# Extract ivector
steps/online/nnet2/extract_ivectors_online.sh --cmd "$cmd" --nj $nj $input_dir $ivector_extractor $input_dir/ivectors > /dev/null || exit 1;

# Compute Log-likelihoods
steps/nnet3/compute_output.sh --cmd "$cmd" --nj $nj --online-ivector-dir $input_dir/ivectors $input_dir $model $output_dir/probs_test > /dev/null || exit 1;

# Split data and make phone-level transcripts
utils/split_data.sh $input_dir $nj > /dev/null || exit 1;
for i in `seq 1 $nj`; do
    utils/sym2int.pl -f 2- $lang/words.txt $input_dir/split${nj}/$i/text > $input_dir/split${nj}/$i/text.int || exit 1;
done

# Convert reference phone transcripts to integer format
utils/sym2int.pl -f 2- $phones $text_phone > $input_dir/text-phone.int || exit 1

# Make align graphs
$cmd JOB=1:$nj $output_dir/ali_test/log/mk_align_graph.JOB.log \
    compile-train-graphs-without-lexicon \
        --read-disambig-syms=$lang/phones/disambig.int \
        $model/tree $model/final.mdl \
        "ark,t:$input_dir/split${nj}/JOB/text.int" \
        "ark,t:$input_dir/text-phone.int" \
        "ark:|gzip -c > $output_dir/ali_test/fsts.JOB.gz" > /dev/null || exit 1;
    echo $nj > $output_dir/ali_test/num_jobs

# Align
steps/align_mapped.sh --cmd "$cmd" --nj $nj --graphs $output_dir/ali_test $input_dir $output_dir/probs_test $lang $model $output_dir/ali_test > /dev/null || exit 1;

# Make a map which converts phones to "pure-phones"
local/remove_phone_markers.pl $lang/phones.txt $input_dir/phones-pure.txt $input_dir/phone-to-pure-phone.int > /dev/null || exit 1;

# Convert transition-id to phone-id
$cmd JOB=1:$nj $output_dir/ali_test/log/ali_to_phones.JOB.log \
    ali-to-phones --per-frame=true $model/final.mdl \
        "ark,t:gunzip -c $output_dir/ali_test/ali.JOB.gz|" \
        "ark,t:|gzip -c >$output_dir/ali_test/ali-phone.JOB.gz" > /dev/null || exit 1;

# Compute GOP
mkdir -p $output_dir/computed
$cmd JOB=1:$nj $output_dir/computed/log/compute_gop.JOB.log \
    compute-gop --phone-map=$input_dir/phone-to-pure-phone.int \
        --skip-phones-string=0:1:2 \
        $model/final.mdl \
        "ark,t:gunzip -c $output_dir/ali_test/ali.JOB.gz|" \
        "ark,t:gunzip -c $output_dir/ali_test/ali-phone.JOB.gz|" \
        "ark:$output_dir/probs_test/output.JOB.ark" \
        "ark,t,scp:$output_dir/computed/gop.JOB.ark,$output_dir/computed/gop.JOB.scp" \
        "ark,t,scp:$output_dir/computed/feat.JOB.ark,$output_dir/computed/feat.JOB.scp" > /dev/null || exit 1;

# Return results to stdout
cat $output_dir/computed/gop.*.ark

rm -rf $output_dir