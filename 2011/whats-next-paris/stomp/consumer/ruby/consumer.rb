require 'rubygems'
require 'stomp'

conn = Stomp::Connection.open('guest', 'guest', 'localhost')
conn.subscribe('/topic/stock.prices')
while mesg = conn.receive
  puts mesg.body
end
