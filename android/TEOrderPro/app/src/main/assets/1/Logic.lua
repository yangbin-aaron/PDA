
package.path = TE_PWD .. 'Config/3/?.lua;' .. package.path
local JSON = require 'JSON'
--local TELuaUtil = require("/storage/emulated/0/Android/data/com.toughegg.demolua/files/TELuaUtil")
--
--TE_OrderDetail = TELuaUtil:readFiles("test.txt")
local orderDetail = JSON:decode(TE_OrderDetail)
--
local TECalculate = require("TECalculate"):new(orderDetail)
resultJson = JSON:encode(TECalculate:CalculateData())
TE_sendJsonText(resultJson)



