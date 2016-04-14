
-- local a = require('stlmap'):create()

local a = {}
local b = {}

function setTable(tlb, key, value)
    tlb[key] = value
    table.insert(tlb,key)
    print('aaaa',#tlb)
end

function include(tlb,key)
	-- body
	for i=1,#tlb do
		if tlb[i] == key then
			return true
		end
	end
	return false
end

function isInTable(value, tbl)
	for k,v in ipairs(tbl) do
		if v == value then
			return true
		end
	end
	return false;
end


function split(str, delimiter)
	local result = {}
	if str == nil or str =='' or delimiter==nil then
		return nil
	end
	
    for match in (str..delimiter):gmatch("(.-)"..delimiter) do
        table.insert(result, match)
    end
    return result
end

-- str = '1'
-- a = split(str,',')

-- for i=1,10 do
-- 	if i ~=3 then
-- 		print('aaaaa',i)
-- 	end
-- end




print('reslut', a)


-- aa= '56b021d8f2180c67b8ea9835'
-- setTable(a,'56b021d8f2180c67b8ea9835','11111')
-- setTable(a,'bb','22222')
-- setTable(a,'cc','33333')

-- setTable(b,'qq','55555')

-- a = {}

print('clude-->>',isInTable('aa',a))

-- print('a',a['56b021d8f2180c67b8ea9835'])
-- print('a size', #a)

-- print('b',b.qq)
-- print('b size', #b)




-- print(stlmap:find('cc',b))
-- print(a:echoAll())
-- print(b:find('cc'))

-- table.insert(a,'b = a')

