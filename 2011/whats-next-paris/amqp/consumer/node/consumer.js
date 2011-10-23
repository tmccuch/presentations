var http = require('http').createServer(handler);
var url  = require('url');
var io   = require('socket.io').listen(http);
var fs   = require('fs');
var util = require('util');
var amqp = require('amqp');

function handler(req, res) {
    var path = url.parse(req.url).pathname;
    switch (path){
    case '/':
        path = '/index.html';
    case '/index.html':
        fs.readFile(__dirname + path, function(err, data){
            if (err) return send404(res);
            res.writeHead(200, {'Content-Type': 'text/html'});
            res.write(data, 'utf8');
            res.end();
        });
        break;
    default: send404(res);
    }
};

send404 = function(res){
    res.writeHead(404);
    res.write('404');
    res.end();
};

io.sockets.on('connection', function(client){
    var connection = amqp.createConnection({'host': '127.0.0.1', 'port': 5672});

    connection.on('ready', function() {
        var args = {'exclusive': true, 'autoDelete': true};

        connection.queue('', args,
                         function(queue) {
                             queue.bind('amq.direct', 'stock.prices');
                             queue.subscribe(function(message) {
                                 client.emit('msg', message.data.toString());
                             });
                         });

    });

    client.on('disconnect', function(){
        util.debug("Client disconnected");
        connection.end();
    });
});
http.listen(8080);