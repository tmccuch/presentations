-module(util).

-export([next_price/0]).

-define(STOCK_SYMBOLS, ["ORCL", "MSFT", "HPQ"]).

next_price() ->
    io_lib:fwrite("~s,~.2f", [lists:nth(
                              random:uniform(
                                length(?STOCK_SYMBOLS)), ?STOCK_SYMBOLS),
                            random:uniform() * 5]).

