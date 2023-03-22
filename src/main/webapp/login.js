
function signupdiv(){
    // document.getElementById("requestdiv").style="display:flex";
    document.getElementsByClassName("login-box")[0].style="height:576px"
    // document.getElementById("eye1").style="top:62.5%"
    
    
}
signupdiv();



function signUp(){
    var name = document.getElementById("Name");
    var email = document.getElementById("Email");
    var number = document.getElementById("number");
    if(name.value != ""  && email.value!="" && number.value!="" || number.value!=0){
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function(){
            console.log(this.readyState)
            if(this.readyState==4 && this.status == 200){
                var i = this.responseText;
                var json = JSON.parse(i);
                console.log(json);
                document.getElementById("Email").style="border-color:white;opacity:1";
                document.getElementById("number").style="border-color:white;opacity:1";

                if(json.statusCode==200){
                    if(json.role=="O"){
                        document.getElementById("expiresdiv").style="display:flex";
                    }else{
                        window.location.href="customer.html";
                    }
                }else{
                    if(json.email){
                        document.getElementById("Email").style="border-color:red;opacity:1";
                    }
                    if(json.number){
                        document.getElementById("number").style="border-color:red;opacity:1";
                    }
                    
                }
            }
        }
        var details ={};
        details.name=name.value;
        details.email=email.value;
        details.number=number.value;
        details.Imp=false;
        for(let cookie of document.cookie.split("; ")){
            if(cookie.split("=")[0]=="role"){
                details.Imp = cookie.split("=")[1]=="O";
                break;
            }
        }
        xhr.open("POST","./Signup");
        xhr.setRequestHeader("Content-type","application/json");
        xhr.send(JSON.stringify(details));
    }else{
        if(email.value==""){
            emailstyle="border-color:red;opacity:1";
        }
        if(name.value==""){
            name.style="border-color:red;opacity:1";
        }
        if(number.value==""){
            number.style="border-color:red;opacity:1";
        }
    }
}



