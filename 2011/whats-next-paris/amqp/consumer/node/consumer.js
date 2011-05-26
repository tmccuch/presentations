var http = require('http');
var url  = require('url');
var io   = require('socket.io');
var fs   = require('fs');
var util = require('util');
var amqp = require('amqp');

var server = http.createServer(function (req, res) {
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
});

send404 = function(res){
    res.writeHead(404);
    res.write('404');
    res.end();
};

var socket = io.listen(server);
socket.on('connection', function(client){
    var connection = amqp.createConnection({'host': '127.0.0.1', 'port': 5672});

    connection.on('ready', function() {
        var queue = connection.queue('', {'exclusive'  : true,
                                          'autoDelete' : true});

        queue.bind('amq.direct', 'stock.prices');

        queue.subscribe(function(message) {
            client.send(message.data.toString());
        });

    });

    client.on('disconnect', function(){
        util.debug("Client disconnected");
        connection.end();
    });
});
server.listen(8080);