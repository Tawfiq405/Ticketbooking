// let ws;
var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function(){
                if(this.readyState==4 && this.status == 200){
                    var url = "ws://"+window.location.hostname+":8080/MTbooking/websocket/"+(""+(JSON.parse(this.responseText).Id));
ws = new WebSocket(url);
ws.onmessage = function(message){
    var msg = message.data;
    var json = JSON.parse(msg);
    switch(json.purpose){
        case "ADDTHEATRE":
            var arr = [];
            json.rating=5;
            arr.push(json);
            window.addtheatreintopanel(arr);
            break;
        case "ADDSCREEN":
            var arr = [];
            arr.push(json);
            window.addscreentopanel(arr);
            break;
    }
}
console.log(ws);
                }
            }
            xhr.open("GET","./cinimas/FindName");
            xhr.setRequestHeader("Content-type","application/json");
            xhr.send();








