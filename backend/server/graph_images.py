import tensorflow as tf
import os
import random
# Suppress warnings
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

class Data():
	def __init__(self, batch_size, epochs, test_size, graph_classes, evaluate=False):
		self.batch_size = batch_size
		self.epochs = epochs
		self.graph_classes = graph_classes
		self.amount_graph_classes = len(self.graph_classes)
		self.start = 0
		self.end = self.start + self.batch_size
		# Step 1
		if not evaluate:
			self.filenames, self.labels = self.__create_lists()
			# test_data
			self.test_filenames = None
			self.test_labels = None
			self.test_size = test_size

	def __generate_file_paths(self, data_type='training'):
		graphs = []
		if data_type != 'test':
			total_images = self.batch_size*self.epochs
		else: # data_type == 'testing'
			total_images = self.test_size
		for j, graph_class in enumerate(self.graph_classes):
			path = os.path.join(os.path.join(os.path.join(os.getcwd(), 
				'images'), graph_class), data_type)
			graphs += [(os.path.join(path, 'img_' + str(i) + '.png'), j)
		 for i in range(total_images//self.amount_graph_classes)]
		return graphs

	def __create_lists(self):
		# Step 1
		graphs = self.__generate_file_paths()
		# print(graphs)
		random.shuffle(graphs)
		# print(graphs)
		filenames = [pair[0] for pair in graphs]
		labels = [pair[1] for pair in graphs]
		# print(filenames)
		# print(labels)
		# labels = tf.one_hot(labels, 2)
		# print(labels)
		# with tf.Session() as sess:
		# 	print(sess.run(labels))
		return filenames, labels

	def __create_queue(self, filenames):
		# Step 2
		filename_queue = tf.train.string_input_producer(filenames)
		# print('filename_queue', filename_queue)
		return filename_queue

	def __read_and_decode(self, filename_queue):
		# Step 3
		reader = tf.WholeFileReader()
		# print('reader', reader)
		filename, content = reader.read(filename_queue)
		# print("filename", filename)
		# print("content", content)
		image = tf.image.decode_png(content, channels=1)
		# print("image", image)
		image = tf.cast(image, tf.float32)
		# print("image", image)
		resized_image = tf.image.resize_images(image, [28, 28])
		resized_image = resized_image / 255.0
		# print("resized_image", resized_image)
		return resized_image, filename

	def next_batch(self, test_data=False):
		# Step 2
		# print("CURRENT BATCH\n\n")
		if not test_data:
			# print("self_filenames", self.filenames)
			# print("start", self.start)
			# print("end", self.end)
			filenames = self.filenames[self.start:self.end]
			# print("filenames", filenames)
			# print("len", len(filenames))
			labels = self.labels[self.start:self.end]
			labels = tf.one_hot(labels, self.amount_graph_classes)
			batch_size = self.batch_size
		else: # if test_data
			filenames = self.test_filenames
			labels = self.test_labels
			batch_size = self.test_size
			# print("batch_size", batch_size)
			# print("labels", labels)
			# print("filenames", filenames)

		filename_queue = self.__create_queue(filenames)
		# Step 3: read, decode and resize images
		resized_image, f_name = self.__read_and_decode(filename_queue)
		# step 4: Batching
		# image_batch = tf.train.batch([resized_image], batch_size=batch_size)
		image_batch, f_names = tf.train.batch([resized_image, f_name], batch_size=batch_size)
		# print('image_batch', image_batch)
		# print('label_batch', label_batch)
		# return image_batch, label_batch
		with tf.Session() as sess:
			coord = tf.train.Coordinator() 
			threads = tf.train.start_queue_runners(coord=coord) 
			tf.train.start_queue_runners()
			image_batch = tf.reshape(image_batch, [batch_size, 784])
			i_batch = sess.run(image_batch)
			# print("f_name", sess.run(f_name))
			# print("f_names", sess.run(f_names))
			f_names = sess.run(f_names)
			lbs = sess.run(self.__fix_labels_v2(i_batch))
			# print("lbs", lbs)
			# print(i_batch.shape)
			# print(i_batch[0])
			# label_batch = tf.reshape(label_batch, [12, 2])
			l_batch = lbs
			# label_batch = labels
			# l_batch = sess.run(label_batch)

			# print(l_batch.shape)
			# print(l_batch)
			coord.request_stop()
			coord.join(threads)
			# if self.start % 580 == 0:
			# 	# print(l_batch.shape)
			# 	# print(l_batch[0])
			# 	# print(i_batch.shape)
			# 	# print(i_batch[0])
			# 	a = tf.reshape(i_batch[0], [28, 28, 1])
			# 	a = tf.cast(a, tf.uint8)*255
			# 	# print(sess.run(a))
			# 	full_path = os.path.join(os.getcwd(), 'test2.png')
			# 	with open(full_path, 'bw') as f:
			# 		# print(image2)
			# 		image = tf.image.encode_png(a)
			# 		image = sess.run(image)
			# 		# print(image)
			# 		f.write(image)
			if not test_data:
				self.start += batch_size
				self.end += batch_size
			return i_batch, l_batch

	def __fix_labels(self, f_names):
		# labels = 0
		labels = [0 if 'asc' in f_name.decode() else 1 for f_name in f_names]
		labels = tf.one_hot(labels, self.amount_graph_classes)
		return labels

	def __fix_labels_v2(self, images):
		# for image in images:
		# 	print(image[1])
		labels = [1 if 0.7 > image[770] > 0.6 else 0 for image in images]
		labels = tf.one_hot(labels, self.amount_graph_classes)
		return labels

	def test_data(self):
		# Step 1
		graphs = self.__generate_file_paths(data_type='test')
		# print(graphs)
		random.shuffle(graphs)
		# print(graphs)
		self.test_filenames = [pair[0] for pair in graphs]
		labels = [pair[1] for pair in graphs]
		# print(filenames)
		# print("TITTA HIT", labels)
		self.test_labels = tf.one_hot(labels, self.amount_graph_classes)
		# print(labels)
		# with tf.Session() as sess:
		# 	print(sess.run(labels))
		return self.next_batch(test_data=True)

	def real_data(self, img_nr):
		filenames = []
		path = os.path.join(os.path.join(os.getcwd(), 'images'), 'real_images')
		filenames += [os.path.join(path, 'r_img_' + str(img_nr) + '.png')]
		f_name_q = self.__create_queue(filenames)
		resized_image, f_name = self.__read_and_decode(f_name_q)
		batch_size = 1
		image_batch = tf.train.batch([resized_image], batch_size=batch_size)
		with tf.Session() as sess:
			coord = tf.train.Coordinator() 
			threads = tf.train.start_queue_runners(coord=coord) 
			tf.train.start_queue_runners()
			image_batch = tf.reshape(image_batch, [batch_size, 784])
			i_batch = sess.run(image_batch)
			coord.request_stop()
			coord.join(threads)
			return i_batch

	def real_data1(self, path):
		# print(path)
		filenames = []
		filenames += [path]
		f_name_q = self.__create_queue(filenames)
		resized_image, f_name = self.__read_and_decode(f_name_q)
		batch_size = 1
		image_batch = tf.train.batch([resized_image], batch_size=batch_size)
		with tf.Session() as sess:
			coord = tf.train.Coordinator() 
			threads = tf.train.start_queue_runners(coord=coord) 
			tf.train.start_queue_runners()
			image_batch = tf.reshape(image_batch, [batch_size, 784])
			i_batch = sess.run(image_batch)
			coord.request_stop()
			coord.join(threads)
			return i_batch

# D = Data(2, 6, 4)
# D.next_batch()
# D.next_batch()