
local TELuaUtil = {}
TELuaUtil._index=TELuaUtil

function TELuaUtil:new()
	-- body
	local self={}
	setmetatable(self,TELuaUtil)
	return self
end

function TELuaUtil:CopyObjectWithSchema(dest,src,schema )
 	-- body
 	if src==nil then
 		return nil
 	end

 	if dest==nil then
 		dest={}
 	end

 	for k,v in pairs(schema) do
		if type(schema[k] == 'table') then
			if(src[k]==nil)then
				dest[k]={}
			end
		end

		if(src[k]==nil) then
			dest[k]=schema[k]
		else 
			dest[k] = src[k]
		end
	end
	print(k,v)
	return dest
end

function TELuaUtil:readFiles( fileName )
	local f = assert(io.open(fileName,'r'))
	local content = f:read("*all")
	f:close()
	return content
end 


return TELuaUtil