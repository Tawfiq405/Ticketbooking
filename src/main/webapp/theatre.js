
var radioset1 = document.getElementsByName("sound");
console.log(radioset1.length)
for(let i=0;i<radioset1.length;i++){
    radioset1[i].parentElement.onclick=function(){
        for(let j=0;j<radioset1.length;j++){
            radioset1[j].checked=false;
            radioset1[j].nextElementSibling.style="background :transparent;";
        }
        this.lastChild.style="background:#F9CF00;";
        this.firstChild.checked=true;
    }
}
var radioset2 = document.getElementsByName("Resolution");
console.log(radioset2.length)
for(let i=0;i<radioset2.length;i++){
    radioset2[i].parentElement.onclick=function(){
        for(let j=0;j<radioset2.length;j++){
            radioset2[j].checked=false;
            radioset2[j].nextElementSibling.style="background :transparent;";
        }
        this.lastChild.style="background:#F9CF00;";
        this.firstChild.checked=true;
    }
}

let seatingtype = {}
function createstructure(){
    var value=0;
    var num=0;
    var radioset3 = document.getElementsByName("dimentiongroupoptions");
    for(let i=0;i<radioset3.length;i++){
        if(radioset3[i].checked==true){
            value=radioset3[i].value;
        }
    }
    console.log(value);
    var rowcount = document.getElementById("rowno").value;
    var seatcount= document.getElementById("seatno").value;
    if(value!=0 && rowcount>0 && seatcount>0 && rowcount<=40 && seatcount<=40){
        var arr=[];
        var checkseating = document.getElementsByName("extraseating");
        for(let j=0;j<checkseating.length;j++){
            if(checkseating[j].checked){
                var ar = [checkseating[j].value,Object.keys(seatingtype[checkseating[j].value])[0],seatingtype[checkseating[j].value][Object.keys(seatingtype[checkseating[j].value])[0]]];
                arr.push(ar);
               num++;
            }
        }
        Object.keys(seatingtype[value]).forEach(key => {
            var ar = [value,key,seatingtype[value][key]];
            arr.push(ar);
        })
        
        createseats(arr,num,rowcount,seatcount);
    }
}



function createseats(arr,num,rowcount,seatcount){
    var div = document.getElementsByClassName("screendiv")[0];
    div.className="screendiv hall";
    div.innerHTML="";
    let count=0;
    arr.reverse().forEach(k=>{
        var cat = document.createElement("div");
        cat.className="catogery";

        var left =  document.createElement("div");
left.className="buttonbox"
        var pluscolumn = document.createElement("input");
        pluscolumn.type="button";
        pluscolumn.value="+";
        pluscolumn.onclick=(e)=>{
            console.log(e.target);
            console.log(e.target.parentElement.nextElementSibling);
            var rows = e.target.parentElement.nextElementSibling.children;
            

            let x=0;
            for(let row of rows){
                if(x++==0 || x==rows.length){
                    continue
                }
                var seat= document.createElement("input");
            seat.type="checkbox";
            seat.checked=true;
            var seatdiv = document.createElement("div");
            seatdiv.className="seat"
seatdiv.onclick=(e)=>{
    console.log(e.target);
    console.log(e.target.firstChild.checked);
    console.log(e.target.style);
   
    
    e.target.classList.toggle('selected');
    e.target.firstChild.checked=!(e.target.firstChild.checked);
}
seatdiv.appendChild(seat);
                row.appendChild(seatdiv);
            }
        }
        left.appendChild(pluscolumn);

        var minuscolumn = document.createElement("input");
        minuscolumn.type="button";
        minuscolumn.value="-";
        // minuscolumn.className=""
        minuscolumn.onclick=(e)=>{
            var rows = e.target.parentElement.nextElementSibling.children;
            let x=0;
            for(let row of rows){
                if(x++==0 || x==rows.length){
                    continue
                }
                row.removeChild(row.firstChild);
            }
        }
        left.appendChild(minuscolumn);
        cat.appendChild(left);

        

        var center = document.createElement("div");
        center.className="rowbox";

        var top =  document.createElement("div");
        top.className="buttonbox"
                var pluscolumn = document.createElement("input");
                pluscolumn.type="button";
                pluscolumn.value="+";
                pluscolumn.onclick=(e)=>{
                    console.log(e.target);
                    console.log(e.target.parentElement.nextElementSibling);
                    var c = e.target.parentElement.nextElementSibling.childElementCount;
                    console.log(c);
                    var row = document.createElement("div");
                    row.className="row";
                    
                    for(let k=0;k<c;k++){
                        var seat= document.createElement("input");
                    seat.type="checkbox";
                    seat.checked=true;
                    var seatdiv = document.createElement("div");
                    seatdiv.className="seat"
                    seatdiv.onclick=(e)=>{
                        console.log(e.target);
                        console.log(e.target.firstChild.checked);
                        console.log(e.target.style);
                        
                        
                        e.target.classList.toggle('selected');
                        e.target.firstChild.checked=!(e.target.firstChild.checked);
                    }
                    seatdiv.appendChild(seat);
                                    row.appendChild(seatdiv);
                    }
                    console.log(row);
                    var p= e.target.parentElement.parentElement
                    p.insertBefore(row,p.firstChild.nextElementSibling);
                }
                top.appendChild(pluscolumn);
        
                var minuscolumn = document.createElement("input");
                minuscolumn.type="button";
                minuscolumn.value="-";
                // minuscolumn.className=""
                minuscolumn.onclick=(e)=>{
                    var row = e.target.parentElement.nextElementSibling;
                    var p = row.parentElement
                    p.removeChild(row);
                }
                top.appendChild(minuscolumn);
                center.appendChild(top);

        var row =0;
        var column=0;
        if(arr.length-num>count){
row =rowcount;
column=seatcount;
        }else{
            row=2;
            column=5;
        }
        count++;
        for(let i=0;i<row;i++){
            var rowdiv = document.createElement("div");
            rowdiv.className="row";
            for(let j=0;j<column;j++){
                var seat= document.createElement("input");
            seat.type="checkbox";
            seat.checked=true;
            
            var seatdiv = document.createElement("div");
            seatdiv.className="seat";
            seatdiv.onclick=(e)=>{
                console.log(e.target);
                console.log(e.target.firstChild.checked);
                console.log(e.target.style);
                e.target.classList.toggle('selected');

                
                e.target.firstChild.checked=!(e.target.firstChild.checked);
            }
            seatdiv.appendChild(seat);
                            // row.appendChild(seatdiv);


            rowdiv.appendChild(seatdiv);
            }
            center.appendChild(rowdiv)

        }
        var bottom =  document.createElement("div");
        bottom.className="buttonbox"
                var pluscolumn = document.createElement("input");
                pluscolumn.type="button";
                pluscolumn.value="+";
                pluscolumn.onclick=(e)=>{
                    console.log(e.target);
                    console.log(e.target.parentElement.previousSibling);
                    var c = e.target.parentElement.previousSibling.childElementCount;
                    console.log(c);
                    var row = document.createElement("div");
                    row.className="row";
                    
                    for(let k=0;k<c;k++){
                        var seat= document.createElement("input");
                    seat.type="checkbox";
                    seat.checked=true;
                    var seatdiv = document.createElement("div");
                    seatdiv.className="seat";
                    seatdiv.onclick=(e)=>{
                        console.log(e.target);
                        console.log(e.target.firstChild.checked);
                        console.log(e.target.style);
                        
                        
                        e.target.classList.toggle('selected');
                        e.target.firstChild.checked=!(e.target.firstChild.checked);
                    }
                    seatdiv.appendChild(seat);
                                    row.appendChild(seatdiv);
                    }
                    console.log(row);
                    var p= e.target.parentElement.parentElement
                    p.insertBefore(row,p.lastChild);
                }
                bottom.appendChild(pluscolumn);
        
                var minuscolumn = document.createElement("input");
                minuscolumn.type="button";
                minuscolumn.value="-";
                // minuscolumn.className=""
                minuscolumn.onclick=(e)=>{
                    var row = e.target.parentElement.previousSibling;
                    var p = row.parentElement
                    p.removeChild(row);
                }
                bottom.appendChild(minuscolumn);
                center.appendChild(bottom);
        cat.appendChild(center);
var right = document.createElement("div");
right.className="buttonbox"
        var pluscolumn = document.createElement("input");
        pluscolumn.type="button";
        pluscolumn.value="+";
        pluscolumn.onclick=(e)=>{
            var rows = e.target.parentElement.previousSibling.children;
            

            let x=0;
            for(let row of rows){
                if(x++==0 || x==rows.length){
                    continue
                }
                var seat= document.createElement("input");
            seat.type="checkbox";
            seat.checked=true;
            var seatdiv = document.createElement("div");
            seatdiv.className="seat"
            seatdiv.onclick=(e)=>{
                console.log(e.target);
                console.log(e.target.firstChild.checked);
                console.log(e.target.style);
                
                
                e.target.classList.toggle('selected');
                e.target.firstChild.checked=!(e.target.firstChild.checked);
            }
            seatdiv.appendChild(seat);
                            row.appendChild(seatdiv);
            }
        }
        right.appendChild(pluscolumn);


        var minuscolumn = document.createElement("input");
        minuscolumn.type="button";
        minuscolumn.value="-";
        // minuscolumn.className=""
        minuscolumn.onclick=(e)=>{
            var rows = e.target.parentElement.previousSibling.children;
           let x=0;
            for(let row of rows){
                if(x++==0 || x==rows.length){
                    continue
                }
                row.removeChild(row.lastChild);
            }
        }
        right.appendChild(minuscolumn);
        cat.appendChild(right);

        div.appendChild(cat);
        var p = document.createElement("p");
        p.innerText=k[2];
        div.appendChild(p);
    });
    document.getElementById("createstr").value="Change structure";
    document.getElementById("addscreenbtn").style="display:block";
    document.getElementById("addscreenbtn").onclick=function(){
        addscreen(arr);
    }
}



function creatingseating(arr,num,rowcount,seatcount){
    var div = document.getElementsByClassName("screendiv")[0];
    div.innerHTML="";
    for(let i=0;i<arr.length;i++){
        var p = document.createElement("p");
        p.innerText=arr[i][2];
        div.appendChild(p);
        var div2 = document.createElement("div");
        div2.className="seatcatogery";
        var btn1 = document.createElement("input");
            btn1.type="button";
            btn1.value="-"
            btn1.onclick=function(){removerow(this)};
            btn1.className="plusminusrow";
            div2.appendChild(btn1);
            var row=0;
            if(i<num){
                row=2;
            }else{
                row=rowcount;
            }
        for(let j=0;j<row;j++){
            var div3 = document.createElement("div");
            div3.className="seatrow";
            var btn1 = document.createElement("input");
            btn1.type="button";
            btn1.value="-"
            btn1.onclick=function(){removeseat(this)};
            btn1.className="plusminusbtn minus";
            div3.appendChild(btn1);
            var column=0;
            if(i<num){
                column=5;
            }else{
                column=seatcount;
            }
            for(let k=0;k<column;k++){
                var div4 = document.createElement("div");
                div4.className="theatreseat";
                div3.appendChild(div4);
            }
            var btn2 = document.createElement("input");
            btn2.type="button";
            btn2.value="+"
            btn2.onclick=function(){addseat(this)};
            btn2.className="plusminusbtn plus";
            div3.appendChild(btn2);
            div2.appendChild(div3);
        }
        var btn1 = document.createElement("input");
            btn1.type="button";
            btn1.value="+"
            btn1.onclick=function(){addrow(this)};
            btn1.className="plusminusrow";
            div2.appendChild(btn1);
        div.appendChild(div2);
    }
    document.getElementById("createstr").value="Change structure";
   console.log("assigned")
    document.getElementById("addscreenbtn").style="display:block";
    document.getElementById("addscreenbtn").onclick=function(){
        console.log("clicked");
        addscreen(arr);

    }
}
function addseat(btn){
    var seat = document.createElement("div");
    seat.className="theatreseat";
    var parent = btn.parentElement;
    if(parent.childElementCount<42){
    var lastChild = parent.lastChild;
    parent.insertBefore(seat, lastChild);
    }

}
function removeseat(btn){
    var parent = btn.parentElement;
    if(parent.childElementCount>3){
        var lastChild = parent.lastChild;
        var secondToLastChild = lastChild.previousSibling;
        parent.removeChild(secondToLastChild);
    }
}

function addrow(btn){
    var parent = btn.parentElement;
    var lastChild = parent.lastChild;
    var seatcount = lastChild.previousSibling.childElementCount-2;

    if(parent.childElementCount<17 && seatcount>0 && seatcount<=40){
    
    var div3 = document.createElement("div");
            div3.className="seatrow";
            var btn1 = document.createElement("input");
            btn1.type="button";
            btn1.value="-"
            btn1.onclick=function(){removeseat(this)};
            btn1.className="plusminusbtn minus";
            div3.appendChild(btn1);
            for(let k=0;k<seatcount;k++){
                var div4 = document.createElement("div");
                div4.className="theatreseat";
                div3.appendChild(div4);
            }
            var btn2 = document.createElement("input");
            btn2.type="button";
            btn2.value="+"
            btn2.onclick=function(){addseat(this)};
            btn2.className="plusminusbtn plus";
            div3.appendChild(btn2);

    
    
    parent.insertBefore(div3, lastChild);
        }
}

function removerow(btn){
    var parent = btn.parentElement;
    if(parent.childElementCount>3){
        var lastChild = parent.lastChild;
        var secondToLastChild = lastChild.previousSibling;
        parent.removeChild(secondToLastChild);
    }
}

function addscreen(arr){
    arr.reverse();
    console.log(arr);
    var details=[];
    var screen = document.getElementsByClassName("hall")[0].children;
    // screen.reverse();
    
   console.log(1);
    var forarr=0;
    for(let i=screen.length-1;i>0;i-=2){
        var rows={};
        var div = screen[i-1].firstChild.nextElementSibling;
        div.removeChild(div.firstChild);
        div.removeChild(div.lastChild);
        var rowno = div.childElementCount;
        for(let row of div.children){
            var seats = {};
            var seatno=0;
            for(let seat of row.children){
                seats[++seatno]=seat.firstChild.checked;
            }
            rows[rowno--]=seats;
        }

        
        var arr1=[arr[forarr++],rows];
        arr1[0].push(forarr-1);
        details.push(arr1);
    }
    console.log(details);





    var json ={};
    json.strcture=details;
    json.name=document.getElementById("screenname").value;
    json.rgb=document.getElementById("rgb").checked;
    for(let d of document.getElementsByName("sound")){
        if(d.checked){
            json.sound=d.value;
            break;
        }
    }
    for(let d of document.getElementsByName("Resolution")){
        if(d.checked){
            json.Resolution=d.value;
            break;
        }
    }
    var v=true;
    Object.keys(json).forEach(key=>{
        if(json[key]=="" || json[key]==undefined){
            v=false;
        }
    })
   if(v){
    json.thId=null;
    var cookies = document.cookie.split("; ");
    for(let cookie of cookies){
        var keyval= cookie.split("=");
        if(keyval[0]=="thId"){
            json.thId=keyval[1];
            break;
        }
    }
    json.purpose="ADDSCREEN";
    ws.send(JSON.stringify(json));
    parent.blockall();
    parent.hideframe();
   }
}

function loadseating(){
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(){
    if(this.readyState==4 && this.status == 200){
        var json = JSON.parse(this.responseText);
        console.log(JSON.parse(json.json))
            if(json.statusCode==200){
                splitseating(JSON.parse(json.json))
            }else if(json.statusCode==401){
                window.location.href="home.html";
            }else{
                console.error(json);
            }            
    }
    }
    xhr.open("GET","./cinimas/impresario/fetchseatinggroup");
    xhr.setRequestHeader("Content-type","application/json");
    xhr.send();
}
function splitseating(json){
    seatingtype=json;
    var group = document.getElementById("groupseating");
    var lux = document.getElementById("luxuryseating");
    var num=0;
    Object.keys(json).forEach(key => {
        // console.log(key)
        // console.log(Object.keys(json).length)
        if(Object.keys(json[key]).length>1){
            var div = document.createElement("div");
            div.className="dimentiongroupoptions";

            var p= document.createElement("p");
            p.innerText="Group "+(++num);
            div.appendChild(p);

            var div2 = document.createElement("div");
            div2.className="radiobtn";

            var input = document.createElement("input");
            input.type="radio";
            input.name="dimentiongroupoptions";
            input.value=key;

            var span = document.createElement("span");

            div2.appendChild(input);
            div2.appendChild(span);
            div.appendChild(div2);

            var ul= document.createElement("ul");
            
            Object.keys(json[key]).forEach(k => {
                var li = document.createElement("li");
                li.innerHTML=json[key][k];
                ul.appendChild(li);
            })
            div.appendChild(ul);
            group.appendChild(div);
        }else{
            var div= document.createElement("div");
            div.className="luxurybox";

            var h6 = document.createElement("h6");
            h6.innerHTML=json[key][Object.keys(json[key])[0]];
            div.appendChild(h6);

            var div2 = document.createElement("div");
            div2.className="checkbox";

            var input = document.createElement("input");
            input.name="extraseating";
            input.type="checkbox";
            input.value=key;
            div2.appendChild(input);

            var p= document.createElement("p");
            p.innerHTML="✔";
            div2.appendChild(p);
            div.appendChild(div2);
            lux.appendChild(div);
        }
        // console.log(+" "+JSON.stringify(json[key]));
      });
      var radioset3 = document.getElementsByName("dimentiongroupoptions");
console.log(radioset3.length)
for(let i=0;i<radioset3.length;i++){
    radioset3[i].parentElement.onclick=function(){
        for(let j=0;j<radioset3.length;j++){
            radioset3[j].checked=false;
            radioset3[j].nextElementSibling.style="background :transparent;";
        }
        this.lastChild.style="background:#F9CF00;";
        this.firstChild.checked=true;
    }
}


for(let i=0;i<document.getElementsByClassName("checkbox").length;i++){
  
    document.getElementsByClassName("checkbox")[i].onclick = function(){
        console.log(document.getElementsByClassName("checkbox")[i].firstChild.checked);
        if(document.getElementsByClassName("checkbox")[i].firstChild.checked){
            document.getElementsByClassName("checkbox")[i].firstChild.checked=false;
            document.getElementsByClassName("checkbox")[i].lastChild.style="color:transparent;";
            document.getElementsByClassName("checkbox")[i].style="box-shadow:  inset 5px 5px 8px 1px rgba(0, 0, 0, 0.2),                6px 6px 15px 3px rgba(48, 46, 54, 1),                inset -5px -5px 8px 0px rgba(77, 71, 100, 0.3),                inset -1px -2px 2px 0px rgba(104, 97, 133, 0.4),                -6px -6px 15px 3px rgba(77, 71, 100, 0.6);";
        }else{
            document.getElementsByClassName("checkbox")[i].firstChild.checked=true;
            document.getElementsByClassName("checkbox")[i].style="box-shadow: 6px 6px 15px 3px rgba(48, 46, 54, 1),1px 1px 5px 1px rgba(48, 46, 54, 0.8),                -6px -6px 15px 3px rgba(77, 71, 100, 0.6),                -1px -1px 2px 1px rgba(77, 71, 100, 0.6);";
            document.getElementsByClassName("checkbox")[i].lastChild.style="color:#F9CF00;";
            }
        console.log(document.getElementsByClassName("checkbox")[i].firstChild.checked)
    }

}
}





























