import os
import tempfile
import shutil

def create_tmp_dir(prefix: str = 'tmp_') -> str:
    """Creates a temporary directory within /tmp (or the current directory)."""
    path = tempfile.mkdtemp(prefix=prefix)
    return path

def remove_dir(path: str) -> None:
    """Removes a directory (even if it contains files)."""
    if os.path.exists(path):
        shutil.rmtree(path)

def get_next_filename(base_dir: str, base_name: str, ext: str) -> str:
    """Generates the next non-existing filename, ex: file_0.wav, file_1.wav, etc."""
    i = 0
    while True:
        candidate = os.path.join(base_dir, f'{base_name}_{i}{ext}')
        if not os.path.exists(candidate):
            return candidate
        i += 1
        
def get_next_subdir(base_dir: str, prefix: str) -> str:
    """Generates the next non-existing dir, ex: dir_0, dir_1, etc."""
    i = 0
    while True:
        candidate = os.path.join(base_dir, f'{prefix}{i}')
        if not os.path.exists(candidate):
            return candidate
        i += 1
