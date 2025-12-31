from pathlib import Path
from collections import Counter
import sys

img_path = Path('../app/src/main/res/drawable')

category_filter = sys.argv[1] if len(sys.argv) > 1 else None

counter = Counter()
category_values = []

for img in img_path.glob('*.png'):
    name = img.stem  # ex: food_orange
    if '_' in name:
        category, value = name.split('_', 1)
        if category_filter:
            if category == category_filter:
                category_values.append(value)
        else:
            counter[category] += 1

if category_filter:
    print(category_values)
else:
    for category, total in counter.items():
        print(f'{category}: {total}')

