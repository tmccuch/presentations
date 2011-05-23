import puka
import threading

queue_name = "stock.prices"

#Callback for message processing
def msg_loop(client, consume_promise, flag):
    while not flag.isSet():
        result = client.wait(consume_promise)
        print result["body"]
        client.basic_ack(result)

# Connect to the broker
client = puka.Client("amqp://localhost")
promise = client.connect()
client.wait(promise)

# Declare the queue
promise = client.queue_declare(queue=queue_name, exclusive=True)
client.wait(promise)

# Start the consumer
consume_promise = client.basic_consume(queue=queue_name)
flag = threading.Event()

# Run the processing callback
thread = threading.Thread(target=msg_loop, args=(client,consume_promise,flag))
thread.start()

# Wait for user input
raw_input()
flag.set()

# Shutdown
thread.join()

#Close the client
promise = client.close()
client.wait(promise)
