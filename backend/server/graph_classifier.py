import tensorflow as tf
import os, random
from graph_images import Data

# Suppress warnings
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

CLASSES = ['linear_ascending', 'linear_descending', 'constant'] # Graph classes
BATCH_SIZE = 3
EPOCHS = 12
TEST_SIZE = 15

x = tf.placeholder(tf.float32, [None, 784]) # Input vector

# W = tf.Variable(tf.zeros([784, 3])) # Weights matrix
W = tf.Variable(tf.truncated_normal([784, 3], stddev=0.1))

# b = tf.Variable(tf.zeros([3])) # Bias vector
b = tf.Variable(tf.truncated_normal([3], stddev=0.1))

y = tf.nn.softmax(tf.matmul(x, W) + b) # Predicted graph classes

y_ = tf.placeholder(tf.float32, [None, 3]) # Actual graph classes
# cross_entropy = tf.reduce_mean(-tf.reduce_sum(y_ * tf.log(y), reduction_indices=[1]))
cross_entropy = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(labels=y_, logits=y))

train_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)
sess = tf.InteractiveSession()
tf.global_variables_initializer().run()

data = Data(BATCH_SIZE, EPOCHS, TEST_SIZE, CLASSES) # Initialize training data
for _ in range(EPOCHS):
  batch_xs, batch_ys = data.next_batch() # Load next batch
  print("Epoch:", _)
  sess.run(train_step, feed_dict={x: batch_xs, y_: batch_ys}) # Train the nn

TEST_IMAGES, TEST_LABELS = data.test_data() # Initialize test data
print("TEST_LABELS", TEST_LABELS)
print("Testing:")
correct_prediction = tf.equal(tf.argmax(y,1), tf.argmax(y_,1))
print("correct_prediction:", sess.run(correct_prediction, feed_dict={x: TEST_IMAGES, y_: TEST_LABELS}))
accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
print("accuracy:", accuracy)

print(sess.run(accuracy, feed_dict={x: TEST_IMAGES, y_: TEST_LABELS})) # Calculate accuracy
