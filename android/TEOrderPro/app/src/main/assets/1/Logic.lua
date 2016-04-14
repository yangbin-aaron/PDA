-- #! /usr/bin/env lua
 
 -- Init the environment 

 package.path =string.format("%s/?.lua;%s",TE_PWD,package.path)
 print('\n Version : 3.1.0.0')
 print('\n Date : 2016-03-24')
 print('\n PWD --->>>>>>: \n ' .. TE_PWD .. '\n')
 print('\n TE_OrderDetail------->>>>>>>: \n '..TE_OrderDetail..'\n')
local JSON = require("JSON")

--function readFiles( fileName )
--	local f = assert(io.open(fileName,'r'))
--	local content = f:read("*all")
--	f:close()
--	return content
--end

--TE_OrderDetail=readFiles("test.txt")

local orderDetail = JSON:decode(tostring(TE_OrderDetail))
local TEResolveStr= require('TEResolveStr'):new(orderDetail)
print('\n str--->>>>\n' , TEResolveStr:resolver() )

local TECalculate = require("TECalculate"):new(TEResolveStr:resolver())
resultJson=JSON:encode(TECalculate:CalculateData())
print('\n resultJson------->>>>>>>: \n '..resultJson..'\n')
-- TE_sendJsonText(resultJson)


