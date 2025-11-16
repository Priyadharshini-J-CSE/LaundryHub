import os
import re

# Add viewport to all HTML files
def add_viewport_to_html_files(directory):
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.html'):
                file_path = os.path.join(root, file)
                with open(file_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                
                # Check if viewport already exists
                if 'viewport' not in content:
                    # Add viewport after charset
                    content = re.sub(
                        r'(<meta charset="UTF-8">)',
                        r'\1\n    <meta name="viewport" content="width=device-width, initial-scale=1.0">',
                        content
                    )
                    
                    with open(file_path, 'w', encoding='utf-8') as f:
                        f.write(content)
                    print(f"Updated: {file_path}")

# Run the script
templates_dir = "src/main/resources/templates"
add_viewport_to_html_files(templates_dir)
print("Mobile viewport added to all templates!")