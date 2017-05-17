# from tensorflow.examples.tutorials.mnist import input_data
# mnist = input_data.read_data_sets("MNIST_data/", one_hot=True)
import tensorflow as tf
import os, random
from graph_images import Data

# Suppress warnings
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

def train():
  CLASSES = ['linear_ascending', 'linear_descending'] # Graph classes
  N_CLASSES = len(CLASSES)
  BATCH_SIZE = 32
  EPOCHS = 25 # 20
  TEST_SIZE = 80

  def weight_variable(shape):
    initial = tf.truncated_normal(shape, stddev=0.1)
    return tf.Variable(initial)

  def bias_variable(shape, name):
    initial = tf.constant(0.1, shape=shape)
    return tf.Variable(initial, name=name)

  def conv2d(x, W):
    return tf.nn.conv2d(x, W, strides=[1, 1, 1, 1], padding='SAME')

  def max_pool_2x2(x):
    return tf.nn.max_pool(x, ksize=[1, 2, 2, 1],
                          strides=[1, 2, 2, 1], padding='SAME')

  x = tf.placeholder(tf.float32, [None, 784], name='x')
  y_ = tf.placeholder(tf.float32, [None, N_CLASSES])

  W_conv1 = weight_variable([5, 5, 1, 32])
  b_conv1 = bias_variable([32], 'b_conv1')

  x_image = tf.reshape(x, [-1,28,28,1])

  h_conv1 = tf.nn.relu(conv2d(x_image, W_conv1) + b_conv1)
  h_pool1 = max_pool_2x2(h_conv1)

  W_conv2 = weight_variable([5, 5, 32, 64])
  b_conv2 = bias_variable([64], 'b_conv2')

  h_conv2 = tf.nn.relu(conv2d(h_pool1, W_conv2) + b_conv2)
  h_pool2 = max_pool_2x2(h_conv2)

  W_fc1 = weight_variable([7 * 7 * 64, 1024])
  b_fc1 = bias_variable([1024], 'b_fc1')

  h_pool2_flat = tf.reshape(h_pool2, [-1, 7*7*64])
  h_fc1 = tf.nn.relu(tf.matmul(h_pool2_flat, W_fc1) + b_fc1)

  keep_prob = tf.placeholder(tf.float32, name='keep_prob')
  h_fc1_drop = tf.nn.dropout(h_fc1, keep_prob)

  W_fc2 = weight_variable([1024, N_CLASSES])
  b_fc2 = bias_variable([N_CLASSES], 'b_fc2')

  y_conv = tf.add(tf.matmul(h_fc1_drop, W_fc2), b_fc2, name='y_conv')

  cross_entropy = tf.reduce_mean(
      tf.nn.softmax_cross_entropy_with_logits(labels=y_, logits=y_conv))
  train_step = tf.train.AdamOptimizer(1e-4).minimize(cross_entropy) # 1e-3?
  correct_prediction = tf.equal(tf.argmax(y_conv,1), tf.argmax(y_,1))
  accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
  sess = tf.InteractiveSession()
  sess.run(tf.global_variables_initializer())

  data = Data(BATCH_SIZE, EPOCHS, TEST_SIZE, CLASSES) # Initialize training data

  for i in range(EPOCHS):
    batch_xs, batch_ys = data.next_batch() # Load next batch
    print("Epoch:", i)
    # batch = mnist.train.next_batch(50)
    if i%5 == 0:
      # print(batch_xs[0])
      train_accuracy = accuracy.eval(feed_dict={
          x:batch_xs, y_: batch_ys, keep_prob: 1.0})
      print("step %d, training accuracy %g"%(i, train_accuracy))
      # print("labels %s" %batch_ys)
    train_step.run(feed_dict={x: batch_xs, y_: batch_ys, keep_prob: 0.5})

  # tf.add_to_collection('y_conv', y_conv)
  saver = tf.train.Saver()
  path = os.path.join(os.getcwd(), 'my_model')
  saver.save(sess, path)

  TEST_IMAGES, TEST_LABELS = data.test_data() # Initialize test data
  # print("TEST_LABELS", TEST_LABELS)
  print("Testing:")
  print("test accuracy %g"%accuracy.eval(feed_dict={
      x: TEST_IMAGES, y_: TEST_LABELS, keep_prob: 1.0}))
  # print("Test Evaluation:", sess.run(y_conv, feed_dict={x: TEST_IMAGES, keep_prob: 1.0}))

  # for i in range(6):
    # real_data = data.real_data(i)
    # print("Real data:", real_data)
    # print("img_nr: " + str(i))
    # print("Evaluation:", sess.run(y_conv, feed_dict={x: real_data, keep_prob: 1.0}))

def evaluate(path_1):
  sess = tf.Session()
  path = os.path.join(os.getcwd(), 'my_model.meta')
  saver = tf.train.import_meta_graph(path)
  path = os.path.join(os.getcwd(), 'my_model')
  saver.restore(sess, path)
  # print(sess.run('b_conv1:0'))
  graph = tf.get_default_graph()
  x = graph.get_tensor_by_name('x:0')
  keep_prob = graph.get_tensor_by_name('keep_prob:0')
  evl = graph.get_tensor_by_name('y_conv:0')
  CLASSES = ['linear_ascending', 'linear_descending'] # Graph classes
  N_CLASSES = len(CLASSES)
  BATCH_SIZE = 32
  EPOCHS = 25 # 20
  TEST_SIZE = 80
  data = Data(BATCH_SIZE, EPOCHS, TEST_SIZE, CLASSES, evaluate=True)
  real_data = data.real_data1(path_1)
  result = sess.run(evl, feed_dict={x: real_data, keep_prob: 1.0})
  # print(result)
  result = result[0]
  if result[0] > result[1]:
    return CLASSES[0]
  else: #result[1] > result[0]
    return CLASSES[1]

# train()
#evaluate()