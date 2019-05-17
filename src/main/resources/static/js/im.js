/**
 * 长轮询即时通讯
 */
const IM =(function(jQuery){
    const $ =jQuery;
    return {
        poll : function(callback) {
            $.ajax({
                url:"/im/poll",
                type: "POST",
                success: function (data) {
                    console.log(JSON.stringify(data));
                    if (data && callback){
                        callback(data);
                    }
                    IM.poll(callback);
                },
                error: function (err) {
                    console.log(JSON.stringify(err));
                    setTimeout(IM.poll,2000);
                }
            });
        },

        subscribe : function(channel) {
            $.post('/im/subscribe',{
                channel:channel
            },function (e) {
                console.log(e);
            })
        },
        unsubscribe : function (channelName) {
            let channel = channelName?{channel:channelName}:null;
            $.post('/im/unsubscribe',channel,function (e) {
                console.log(e);
            })
        },
        send : function(channel,text) {
            $.ajax({
                url:"/im/emit",
                type: "POST",
                data: JSON.stringify({
                    channel:channel,
                    message:{
                        text:text
                    }
                }),
                success: function (e) {
                    console.log(e);
                },
                dataType: "json",
                contentType: "application/json"
            });
        }
    };
})(jQuery);



