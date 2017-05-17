import tensorflow as tf
import numpy as np
import os
import random

# Suppress warnings
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

IMAGE_FOLDER = os.path.join(os.getcwd(), 'images')
EXTENSION = '.png'
ASC_INDEX = -1 # -1
DESC_INDEX = -1 # -1

def generate_graph(size, multiplier):
	global ASC_INDEX, DESC_INDEX
	# ax+b
	a = random.randint(10, 500) / 100.0 * random.choice([-1, 1])
	if a < 0:
		label = 'linear_descending'
		DESC_INDEX += 1
		index = DESC_INDEX
	else: # a > 0
		label = 'linear_ascending'
		ASC_INDEX += 1
		index = ASC_INDEX
	b = random.randint(50, 500) / 100.0 * random.choice([-1, 1])
	# print("a:", a)
	# print("b:", b)
	# print(str(a) + " * " + "x" + " + " + str(b))
	y_values = [linear_function(a, b * multiplier, x) for x in range(-size, size)]
	return y_values, label, index

def linear_function(a, b, x):
	return a*x+b

def generate_image(data_type):
	image_tensor = [[[255] for i in range(28)] for j in range(28)] # Create blank image
	# print(image_tensor)
	size = 14
	multiplier = 4
	graph, label, index = generate_graph(size*multiplier, multiplier)
	# image_tensor[27][0][0] = 0 # [row][col][0]
	# if label == 'linear_ascending':
	# 	image_tensor = [[[0] for i in range(28)] for j in range(28)] # Create black image
	for x, y in enumerate(graph):
		# print(x)
		y_int = int(y)//multiplier + size
		if 0 <= y_int <= 27:
			image_tensor[27 - y_int][x//multiplier][0] = 0

	if label == 'linear_ascending':
		image_tensor[27][14][0] = 85
	else: #label == 'linear_descending'
		image_tensor[27][14][0] = 170

	write_image(image_tensor, label, index, data_type)
	return

def write_image(image_tensor, graph_type, nr, data_type):
	full_path = os.path.join(os.path.join(os.path.join(IMAGE_FOLDER, graph_type), data_type), 'img_' + str(nr) + EXTENSION)
	with open(full_path, 'bw') as f:
		# print(image2)
		image = tf.image.encode_png(image_tensor)
		with tf.Session() as sess:
			image = sess.run(image)
			# print(image)
			f.write(image)

def generate_images(n, data_type):
	for i in range(n):
		tmp_img = generate_image(data_type)
		del tmp_img
		if i % 50 == 0:
			print("Done with %s images" %i)

TRAINING_IMAGES = 1000
TEST_IMAGES = 100
print("*********************************")
print("Generating %s training images" %TRAINING_IMAGES )
generate_images(TRAINING_IMAGES, 'training')
print("*********************************")
print("Generating %s test images" %TEST_IMAGES)
generate_images(TEST_IMAGES, 'test')
