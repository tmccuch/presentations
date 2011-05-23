import puka

client = puka.Client("amqp://localhost")
promise = client.connect()
client.wait(promise)

print "Connected"

promise = client.close()
client.wait(promise)
