-- table 保存键值对

stlmap = {}
stlmap._index = stlmap

function stlmap:create()
	self.num =0
	self.data = {}
	return self
end



function setDefault(default, t, d)
    default[t] = d
    -- setmetatable(t, mt)
    table.insert(default,t)
end


function stlmap:insert(key,obj)
	-- print('key',key)
	-- print('obj',obj)
	self.num = self.num + 1
	self.data[self.num] = {}
	self.data[self.num]= {key = obj}

end

function stlmap:find(key)
	print('self.num',self.num)
	for i=1,self.num do
		if self.data[i].key == key then
			return self.data[i].obj
		end
	end
end

function stlmap:echoAll(tab)
	return self.num
end

function stlmap:include(key)
	-- body
	for i=1,self.num do
		if self.data[i].key == key then
			return true
		end
	end
	return false
end

return stlmap
