-module(publisher).

-export([start/0, stop/1]).
-export([loop/1]).

-include_lib("amqp_client/include/amqp_client.hrl").

-define(STOCK_SYMBOLS, ["ORCL", "MSFT", "HPQ"]).

start() ->
    random:seed(now()),
    {ok, Connection} = amqp_connection:start(#amqp_params_network{}),
    {ok, Channel} = amqp_connection:open_channel(Connection),
    spawn(?MODULE, loop, [Channel]).

stop(Pid) ->
    Pid ! stop.

loop(Channel) ->
    PublishMethod =
        #'basic.publish'{exchange    = <<"">>,
                         routing_key = <<"stock.prices">>},

    Msg = #amqp_msg{payload = list_to_binary(next_price())},

    amqp_channel:cast(Channel, PublishMethod, Msg),

    timer:sleep(500),
    receive
        stop -> ok
    after
        1 -> loop(Channel)
    end.

next_price() ->
    io_lib:fwrite("~s,~.2f", [lists:nth(
                              random:uniform(
                                length(?STOCK_SYMBOLS)), ?STOCK_SYMBOLS),
                            random:uniform() * 5]).



