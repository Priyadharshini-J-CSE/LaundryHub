import os
import re

def fix_navbar_in_html_files(directory):
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.html'):
                file_path = os.path.join(root, file)
                with open(file_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                
                # Skip login and register pages (no navbar)
                if 'login.html' in file_path or 'register.html' in file_path:
                    continue
                
                # Check if navbar exists and needs mobile fix
                if 'navbar' in content and 'navbar-toggler' not in content:
                    # Fix navbar structure for mobile
                    content = re.sub(
                        r'(<nav class="navbar navbar-expand-lg[^>]*>)\s*(<div class="container[^>]*>)\s*(<a class="navbar-brand"[^>]*>[^<]*</a>)\s*(<div class="navbar-nav[^>]*>)',
                        r'\1\n        \2\n            \3\n            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">\n                <span class="navbar-toggler-icon"></span>\n            </button>\n            <div class="collapse navbar-collapse" id="navbarNav">\n                <div class="navbar-nav ms-auto">',
                        content
                    )
                    
                    # Fix closing tags
                    content = re.sub(
                        r'(</a>\s*)(</div>\s*</div>\s*</nav>)',
                        r'\1                </div>\n            </div>\n        </div>\n    </nav>',
                        content
                    )
                    
                    with open(file_path, 'w', encoding='utf-8') as f:
                        f.write(content)
                    print(f"Fixed navbar: {file_path}")

# Run the script
templates_dir = "src/main/resources/templates"
fix_navbar_in_html_files(templates_dir)
print("All navbars fixed for mobile!")