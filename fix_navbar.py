import os
import re

def add_bootstrap_js_to_html_files(directory):
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.html'):
                file_path = os.path.join(root, file)
                with open(file_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                
                # Check if Bootstrap JS already exists
                if 'bootstrap.bundle.min.js' not in content:
                    # Add Bootstrap JS before closing body tag
                    content = re.sub(
                        r'</body>',
                        r'    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>\n</body>',
                        content
                    )
                    
                    with open(file_path, 'w', encoding='utf-8') as f:
                        f.write(content)
                    print(f"Updated: {file_path}")

# Run the script
templates_dir = "src/main/resources/templates"
add_bootstrap_js_to_html_files(templates_dir)
print("Bootstrap JS added to all templates!")