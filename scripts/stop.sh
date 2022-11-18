echo "开始关闭进程:"
port='8081'
pid=`netstat -anp|grep $port|awk '{printf $7}'|cut -d/ -f1`
if test -z $pid
then
   echo -e "\033[42;31m当前端口${port}没有被占用!\033[0m"
else
   echo -e "\033[47;31m当前端口${port}被进程${pid}占用!\033[0m"                                                                     
   echo -e "\033[42;31m关闭进程${pid}\033[0m"
   kill -9 $pid
fi