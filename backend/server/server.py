import os, random
from flask import Flask, request
from werkzeug.utils import secure_filename

UPLOAD_FOLDER = os.path.join(os.path.join(os.getcwd(), '..'), 'images')
print("Setting the upload folder to:", UPLOAD_FOLDER)
ALLOWED_EXTENSIONS = set(['.png', '.jpg', '.jpeg'])
IMAGE_NAME = 'tmp_image'
EXTENSION = '.png'

print("Creating app")
app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['IMAGE_NAME'] = IMAGE_NAME
app.config['EXTENSION'] = EXTENSION

@app.route('/')
def hello_world():
	return 'Hello, World!'

# Test with curl -F "filename=FILENAME" -F "file=@FILEPATH" SERVERADDRESS:5000/upload
@app.route('/upload', methods=['GET', 'POST'])
def upload_image():
	response = 'Dummy response'

	if request.method == 'POST':
		f = request.files['file']
		filename = secure_filename(f.filename)
		extension = '.' + filename.rsplit('.', 1)[1].lower()
		if extension not in ALLOWED_EXTENSIONS:
			response = "File extension %s not allowed" % extension
			return response
		full_path = os.path.join(app.config['UPLOAD_FOLDER'], app.config['IMAGE_NAME'] + app.config['EXTENSION'])
		f.save(full_path)
		response = evaluate(f)

	if os.path.isfile(full_path):
		os.remove(full_path)
	else:
		print("Error: %s file not found" % full_path)

	return response

def evaluate(image):
	return random.choice(['Ascending linear function', 'Descending linear function'])

# Test with: curl --data-binary "@FILEPATH" localhost:5000/upload_stream
@app.route('/upload_stream', methods=['GET', 'POST'])
def upload_image2():
	response = 'Dummy response'
	if request.method == 'POST':
		full_path = os.path.join(app.config['UPLOAD_FOLDER'], app.config['IMAGE_NAME'] + app.config['EXTENSION'])
		with open(full_path, 'bw') as f:
			chunk_size = 4096
			while True:
				chunk = request.stream.read(chunk_size)
				if len(chunk) == 0:
					break
				f.write(chunk)
			response = evaluate(f)

	if os.path.isfile(full_path):
		os.remove(full_path)
	else:
		print("Error: %s file not found" % full_path)

	return response

if __name__ == '__main__':
	app.run(host='0.0.0.0')
