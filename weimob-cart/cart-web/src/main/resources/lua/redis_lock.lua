local flag=redis.call('setnx',KEYS[1],ARGV[2])
if(flag) then
      redis.call('expire',KEYS[1],ARGV[1])
end
return flag