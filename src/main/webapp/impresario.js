
function fetchtheatres(){
    var mhr = new XMLHttpRequest();
        mhr.onreadystatechange = function(){
            if(this.readyState==4 && this.status == 200){
                var json = JSON.parse(this.responseText);
                if(json.statusCode==200){
                    if(json.jsonarr.length>0){
                        var box  = document.getElementById("theatrelist");
                        box.innerHTML="";
                        addtheatreintopanel(json.jsonarr);
                    }                    
                }else if(json.statusCode==401){
                    window.location.href="home.html";
                }else{
                    console.error(json)
                }
            }
        }
        mhr.open("get","./cinimas/impresario/MyTheatres");
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send();
}
var currentth;
function fetchscreen(){
    var mhr = new XMLHttpRequest();
        mhr.onreadystatechange = function(){
            if(this.readyState==4 && this.status == 200){
                var json = JSON.parse(this.responseText);
                if(json.statusCode==200){
                    if(json.jsonarr.length>0){
                        addscreentopanel(json.jsonarr);
                    }                
                }else if(json.statusCode==401){
                    window.location.href="home.html";
                }else{
                    console.error(json)
                }
            }
        }
        mhr.open("get","./cinimas/impresario/fetchscreens?thId="+currentth);
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send();
}
fetchtheatres();

function addscreentopanel(jsonarr){
    console.log(jsonarr.length);
    var box  = document.getElementById("theatrescreenleft");
    // box.innerHTML="";
   
    var thId=null;
    var cookies = document.cookie.split("; ");
    for(let cookie of cookies){
        var keyval= cookie.split("=");
        if(keyval[0]=="thId"){
            thId=keyval[1];
            break;
        }
    }
    console.log(thId);
    console.log(currentth);
    for(let json of jsonarr){
        console.log(json.thId);
        if(json.thId!=thId){
            continue;
        }
        var div = document.createElement("div");
        div.id="theatrebox";
        var span1 = document.createElement("span");
        span1.innerHTML=json.name;
        var span2 = document.createElement("span");
        span2.innerHTML=json.sound;
        var span3 = document.createElement("span");
        span3.innerHTML=json.resolutions;
        div.appendChild(span1);
        div.appendChild(span2);
        div.appendChild(span3);
        div.onclick=()=>{
            showstructure(json.scId);
        }
        box.appendChild(div);
    }
}
function showstructure(scId){
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(){
    if(this.readyState==4 && this.status == 200){
        var json = JSON.parse(this.responseText);
        console.log(json)
            if(json.statusCode==200){
               createstructurediv(json.jsonarr);
            }else if(json.statusCode==401){
                window.location.href="home.html";
            }else{
                console.error(json);
            }            
    }
    }
    xhr.open("GET","./cinimas/impresario/fetchscreenstructure?thId="+currentth+"&scId="+scId);
    xhr.setRequestHeader("Content-type","application/json");
    xhr.send();
    // 

}


function hidestructure(btn){
    btn.style="display:none";
}
var seatofthis;
function createstructurediv(jsonarr){
    seatofthis=jsonarr;
    console.log(jsonarr);
    jsonarr.reverse();
    var div = document.getElementById("screenstructurediv");
    div.innerHTML=`<input type="button" class="inputbtn" id="hidescreenstructurebtn" onclick="hidestructure(this.parentElement)" value="Hide">`;
div.style="display:block;background:black;overflow:hidden;";
div.classList.add("hall");
var mnbgh = document.createElement("div");
mnbgh.style=`height: 300px;
width: 1200px;
background-image: url('MT.png');
background: currentColor;
background-position: center center;
margin: auto;
background:grey`
div.appendChild(mnbgh);
    var margin=12;
    var  z=10;
    for(let json of jsonarr){
        var cat= document.createElement("div");
        cat.className="catogery";
        cat.style="flex-direction:column;"
        var r = json[1];
        var rows = Object.entries(r).reverse();
        console.log(rows);
        // let rowno=0;
        for(let row of rows){
            var rowdiv =   document.createElement("div");
            rowdiv.className="row";
            
            var seats = Object.entries(row[1]).reverse();
            var center = Math.ceil(seats.length/2);
            // var columnno=0;
            var x= false;
            var xc=[];
            for(let seat of seats){
                
                var seatdiv =   document.createElement("div");
                seatdiv.className="seat";
                seatdiv.style=`z-index:${z};`;
               if(!seat[1]){
                seatdiv.style.background="none";
               }
                seatdiv.onclick=(e)=>{
                    console.log(e.target);
                }
                rowdiv.appendChild(seatdiv);
                rowdiv.style=`transform:translateY(${(z-20)/2*(-5)}px) scale(${1+(((z-20)/2)/30)});`
            }
            z+=2;
            cat.appendChild(rowdiv);
            
        }
        console.log(cat);
        div.appendChild(cat);
    }
}
var seats = [];
function addtheatreintopanel(jsonarr){
    var box  = document.getElementById("theatrelist");
    for(let json of jsonarr){
        var div = document.createElement("div");
        div.id="theatrebox";
        var span1 = document.createElement("span");
        span1.innerHTML=json.name;
        var span2 = document.createElement("span");
        span2.innerHTML=json.city;
        var span3 = document.createElement("span");
        span3.innerHTML=json.rating;
        div.appendChild(span1);
        div.appendChild(span2);
        div.appendChild(span3);
        div.onclick=()=>{
            thistheatre(json);
        }
        box.appendChild(div);
    }
}
function blockall(){
    document.getElementById("block").style="display:block";
}
function hideframe(){
    var frame = document.getElementById("iframe");
    frame.contentWindow.location.reload();
    frame.style="display:none"
    document.getElementById("block").style="display:none";
}
function hidescreendiv(){
    document.getElementById("theatrescreenleft").innerHTML="";
    document.cookie="thId="+null;
    currentth=null;
    // clearInterval(screeninterval);
    document.getElementById("theatrescreenwholediv").style="display:none";
}
var screeninterval;
function thistheatre(json){
    // document.getElementById("theatrescreenleft").innerHTML=
    document.cookie="thId="+json.thId;
    currentth=json.thId;
    fetchscreen();
    // screeninterval = setInterval(fetchscreen,1000);
    document.getElementById("theatrescreenwholediv").style="display:flex";
    console.log(json);
    theatredetails(json);
}
function theatredetails(json){
    var box = document.getElementById("mytheatredetails");
    box.innerHTML="";

    var h4 = document.createElement("h4");
    h4.innerHTML=json.name;
    box.appendChild(h4); 

    var h6 = document.createElement("h6");
    h6.innerHTML=json.address;
    box.appendChild(h6); 

    var p = document.createElement("p");
    p.innerHTML=json.city+", "+json.state+", "+json.country;
    box.appendChild(p);
    
    var h5 = document.createElement("h5");
    h5.innerHTML=json.landmark;
    box.appendChild(h5);

}
function showamenities(){
    document.getElementById("amenitiesdiv").style="display:flex";
}
function addtheatreform(){
    var form =document.getElementById("addtheatreform");
    form.style="height:148%;margin-top:5%;width:300%;z-index: 5;opacity: 1;";
    getamenities();
}
function hidetheatreform(){
    document.getElementById("addtheatreform").style="";
}
function addscreen(){
    var frame = document.getElementById("iframe");
    frame.contentWindow.location.reload();
    frame.style="display:block"
}
function addtheatretodb(){
    theatre.name = document.getElementById("theatrename").value;
    theatre.address = document.getElementById("theatreaddress").value;
    theatre.city = document.getElementById("city").value;
    theatre.state = document.getElementById("state").value;
    theatre.country = document.getElementById("country").value;
    theatre.landmark = document.getElementById("landtext").innerHTML;
    theatre.purpose = "ADDTHEATRE";
    var amenities = [];
    var inputs= document.getElementsByName("amenities")
    for(let input of inputs){
        if(input.checked==true){
            amenities.push(input.value);
        }
    }
    theatre.amenities=amenities;
    if(theatre.name!="" && theatre.address!="" && theatre.city!="" && theatre.state!="" && theatre.country!="" && theatre.lanmark!=""){
               

    ws.send(JSON.stringify(theatre));



                document.getElementById("addtheatreform").style="";
                Object.keys(theatre).forEach(key => {
                    delete theatre[key];
                  });
                  console.log(theatre);
                document.getElementById("theatrename").value="";
document.getElementById("theatreaddress").value="";
    document.getElementById("city").value="";
     document.getElementById("state").value="";
    document.getElementById("country").value="";
    document.getElementById("landtext").innerHTML="";
}

}
const theatre={};
function getlanmark(){
    var city = document.getElementById("city").value;
    var state = document.getElementById("state").value;
    var country = document.getElementById("country").value;
    if(state!="" && country!=""){
        var details={};
        details.city=city;
        details.state=state;
        details.country=country;

        var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(){
    if(this.readyState==4 && this.status == 200){
        var json = JSON.parse(this.responseText);
        console.log(json)
            if(json.statusCode==200){
                addlanmark(JSON.parse(json.jsonarr));
            }else if(json.statusCode==401){
                window.location.href="home.html";
            }else{
                console.error(json);
            }            
    }
    }
    console.log(JSON.stringify(details));
    xhr.open("GET","./cinimas/LoadLocation?details="+encodeURIComponent(JSON.stringify(details)));
    xhr.setRequestHeader("Content-type","application/json");
    xhr.send();
    }else{
        console.log("fghjkl")
    }
}
function addlanmark(jsonarr){
    console.log(jsonarr)
    var box = document.getElementById("Landmarkbox");
    box.style="height:340px;margin-top:5%;width:300%;z-index: 10;opacity: 1;";
    box.innerHTML="";
    for(let json of jsonarr){
        var div = document.createElement("div");
        div.className="locationname";
        div.innerHTML=json.display_name;
        div.onclick=()=>{
            theatre.lat=json.lat;
            theatre.lon=json.lon;
            theatre.lanmark=json.display_name;
            box.style="";
            document.getElementById("landtext").innerHTML=json.display_name;
            console.log(theatre);
        }
        box.appendChild(div);
        console.log(json);
    }
}

function getamenities(){
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(){
    if(this.readyState==4 && this.status == 200){
        var json = JSON.parse(this.responseText);
        console.log(json)
        var json1=JSON.parse(json.amenities);
            if(json.statusCode==200){
                addamenities(json1);
            }else if(json.statusCode==401){
                window.location.href="home.html";
            }else{
                console.error(json);
            }            
    }
    }
    xhr.open("GET","./cinimas/impresario/fetchamenities");
    xhr.setRequestHeader("Content-type","application/json");
    xhr.send();
}

function addamenities(json){
    var box  = document.getElementById("amenitiesdiv");
    Object.keys(json).forEach(key=>{
        // console.log(key+" "+json[key])
        var div = document.createElement("div");
        div.className="moviflex";
        div.innerHTML=json[key].toUpperCase();
        var div1 = document.createElement("div");
        div1.className="checkbox";
        var input = document.createElement("input");
        input.type="checkbox";
        input.name="amenities";
        input.value=key;
        var span = document.createElement("span");
        span.innerHTML="✔";
        div1.appendChild(input);
        div1.appendChild(span);
        div.appendChild(div1);
        box.appendChild(div);
    });
    var button = document.createElement("input");
    button.className="inputbtn addpadd";
    button.type="button";
    button.value="ADD";
    button.onclick=function(){
        document.getElementById("amenitiesdiv").style="display:none";
    }
    box.appendChild(button);
    var checkboxs = document.querySelectorAll("input[type=checkbox]");
for(let i=0;i<checkboxs.length;i++){
    checkboxs[i].parentElement.lastElementChild.style="display:none;"
    checkboxs[i].parentElement.onclick=function(){
        if(checkboxs[i].checked==true){
            checkboxs[i].checked=false;
            checkboxs[i].parentElement.lastElementChild.style="display:none;"
        }else{
            checkboxs[i].checked=true;
            checkboxs[i].parentElement.lastElementChild.style="";
        }
    }
}
}

function buymovies(){
    
    var mhr = new XMLHttpRequest();
    mhr.onreadystatechange = function(){
        if(this.readyState==4 && this.status == 200){
            var json = JSON.parse(this.responseText);
            if(json.statusCode==200){
                if(json.jsonarr.length>0){
                    console.log(json.jsonarr);
                    addmovieintopanel(json.jsonarr);
                }                    
            }else if(json.statusCode==401){
                window.location.href="home.html";
            }else{
                console.error(json);
            }
        }
    }
    mhr.open("get","./cinimas/impresario/DistributorMovies?count=0");
    mhr.setRequestHeader("Content-type","application/json");
    mhr.send();
}

function addmovieintopanel(jsonarr){
    console.log("changed")
    var div = document.getElementById("showdistributormovies");
    div.style="display:flex;height: 892px;width: 95%;";
    div.innerHTML=`<input value="Back" type="button" class="inputbtn" onclick="hidethismovieform(this,2)">`;
    if(jsonarr.length>0){
        
    for(let json of jsonarr){
        var box = document.createElement("div");
        box.id="impremoviebuydiv"
        var img = document.createElement("div");
        img.id="buymovieimg";
        img.style=`background-image:url('${json.smallpath[Object.keys(json.smallpath)[0]].src}');background-position: center center;background-size:contain;background-repeat:no-repeat;backdrop-filter: blur(15px);`;
        // img.
       
        var img1 = document.createElement("div");
        img1.id="buymovieimg";
        img1.style=`background-image:url('${json.smallpath[Object.keys(json.smallpath)[0]].src}');background-position: center center;background-size:cover;`;
        img1.appendChild(img);
        // img.
        box.appendChild(img1);
        var p1 = document.createElement("p");
        p1.innerHTML=json.name+" ( "+json.ratings+" )"
        box.appendChild(p1);
        div.appendChild(box);
        var input = document.createElement("input");
        input.value="BUY";
        input.className="inputbtn";
        input.type="button";
        input.style="text-align:center;font-weight:bolder;color:rgba(255,255,255,0.3);border-radius:10px"
        input.onclick=function(){
            showbuymovieform(json);
            
        }
        box.appendChild(input);
    }
    }else{
        div.innerHTML="<h1>no movies</h1>";
    }
    
}
function showbuymovieform(json){
    showsuggettion();
    document.getElementById("buymovieform").style="display:flex";

    // document.getElementById("buymoviedeatilsimg").src=json.smallpath[Object.keys(json.smallpath)[0]].src;
    var img = document.createElement("div");
        // img.id="buymovieimg";
        img.style=`background-image:url('${json.smallpath[Object.keys(json.smallpath)[0]].src}');background-position: center center;background-size:contain;background-repeat:no-repeat;backdrop-filter: blur(15px);height:100%;width:100%`;
        // img.
       
        var img1 = document.createElement("div");
        // img1.id="buymovieimg";

        img1.style=`background-image:url('${json.smallpath[Object.keys(json.smallpath)[0]].src}');background-position: center center;background-size:cover;height:100%;width:100%;    height: 90%;
        width: 90%;
        border-radius: 10px;
        box-shadow: -3px -3px 6px 0 rgba(255, 255, 255, 0.2), 6px 6px 12px 0 rgba(0,0,0,0.3);
        border: none;
        outline: none;`;
        img1.appendChild(img);
        img1.id="buymoviedeatilsimg";
        // img.
        document.getElementById("buymoviedeatilsimg").style="display:none";
        document.getElementById("buymoviedeatilsimgdiv").innerHTML="";
        document.getElementById("buymoviedeatilsimgdiv").appendChild(img1);

    var detailsdiv = document.getElementById("buymoviedetailsdisplay");
    detailsdiv.innerHTML="";

    var p1 = document.createElement("p");
    p1.innerHTML=json.name+" ( "+json.ratings+" )";
    p1.style="font-size:2.5rem;"
    detailsdiv.appendChild(p1);
    var p2 = document.createElement("p");
    var s = "Languages : ";
    for(let lan of Object.entries(json.language)){
        s+=lan[1]+", "
    }
    p2.innerHTML=s.slice(0,s.length-2);
    detailsdiv.appendChild(p2);

    var p3 = document.createElement("p");
    s = "Genre : ";
    for(let gen of Object.entries(json.genre)){
        s+=gen[1]+", "
    }
    p3.innerHTML=s.slice(0,s.length-2);
    detailsdiv.appendChild(p3);

    var p4 = document.createElement("p");
    p4.innerHTML=json.date+" | "+json.hours+" hours "+json.min+" min";
    detailsdiv.appendChild(p4);

    var p5 = document.createElement("p");
    p5.innerHTML="Cast : "+json.hero+" | "+json.heroine+" | "+json.villan
    detailsdiv.appendChild(p5);

    var p6 = document.createElement("p");
    p6.innerHTML="Director : "+json.director;
    detailsdiv.appendChild(p6);

    var p7 = document.createElement("p");
    p7.innerHTML="Music Director : "+json.musicdirector;
    detailsdiv.appendChild(p7);

    var p8 = document.createElement("p");
    p8.innerHTML="Synopsis";
    p8.style="font-size:1.5rem;"
    detailsdiv.appendChild(p8);

    var p9 = document.createElement("p");
    p9.innerHTML=json.synopsis;
    detailsdiv.appendChild(p9);

    // undefined
    var per = (json.baseprice / json.capacity);
    var p10 = document.getElementById("p10");
    p10.innerHTML="For 0 - 100 Seats "+(per*100)+" rs";
    var slider = document.getElementById("pricerange");
    slider.addEventListener("input",function(){
        console.log(slider.value)
        ;
        p10.innerHTML="For "+(slider.value-100)+" - "+slider.value+" Seats "+(per * slider.value)+" rs";
    });
    document.getElementById("listofbuyedmovie").innerHTML=`<input type="button" class="inputbtn addmovielistbtn"  value="+">
    <input type="button" class="inputbtn" style="position: absolute;bottom: 10px;right: 10px;" value="Buy" onclick="buyallmovie(this)">`
    document.getElementsByClassName("addmovielistbtn")[0].onclick=function(){
        
        document.getElementById("movietitle").innerHTML=json.name+" ( "+json.ratings+" )";
        var id = json.Id;
        var name =json.name;
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function(){
        if(this.readyState==4 && this.status == 200){
            
            var json = JSON.parse(this.responseText);
                if(json.statusCode==200){
                    buythemovie(id,JSON.parse(json.languages),per,name);
                }else if(json.statusCode==401){
                    window.location.href="home.html";
                }else{
                    console.error(json);
                }            
        }
        }
        xhr.open("GET","./cinimas/impresario/fetchlanguages?Id="+json.Id);
        xhr.setRequestHeader("Content-type","application/json");
        xhr.send();
    }
    
}

function showsuggettion(){
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(){
    if(this.readyState==4 && this.status == 200){
        var json = JSON.parse(this.responseText);
        if(json.statusCode==200){
          createshowbuysuggets(json.screen,json.buyedshow);
        }else if(json.statusCode==401){
            window.location.href="home.html";
        }else{
            console.error(json);
        }    
    }
    }
    xhr.open("GET","./cinimas/impresario/suggetions");
    xhr.setRequestHeader("Content-type","application/json");
    xhr.send();
}

var show;

function createshowbuysuggets(screen,sho){
    console.log(screen);
    show=sho;
    console.log(show)
    Object.keys(screen).forEach(key=>{
        screen[key].forEach(n=>{
            console.log(n);
            Object.keys(show).forEach(k=>{
                if(show[k]==undefined || show[k] == null){
                    show[k]={};
                }
                if(show[k][key]==undefined || show[k][key] == null){
                    
                    console.log(show[k][key]);

                    show[k][key]=0;

                    console.log(show[k][key]);

                }

                console.log(show[k][key]);

                show[k][key]=show[k][key]-7;

                console.log(show[k][key]);
            })
        })
        
    })
    console.log(show);
    Object.keys(show).forEach(key=>{
        Object.keys(show[key]).forEach(k=>{
            show[key][k] = ( show[key][k] * -1 );
            if(show[key][k]<=0){
                delete show[key][k]
            }
        })
    })
    console.log(show);
}

            
function buythemovie(id,language,per1,name){
    console.log(language)
    document.getElementById("buymovieformdiv").style="display:flex;";
    var div = document.getElementById("buymovielanguage");
    div.innerHTML=""
    
    Object.keys(language).forEach(k=>{
        var div1 = document.createElement("div");
        div1.innerHTML=`<span>${language[k]}</span>`;
        div1.className="font"
        var div2 = document.createElement("div");
        div2.className="radiobtn boxroundshadow";

        var input = document.createElement("input");
        input.type="radio";
        input.name="languages";
        input.value=k;

        var span = document.createElement("span");

        div2.appendChild(input);
        div2.appendChild(span);
        div1.appendChild(div2);
        div.appendChild(div1)
    })
    var radioset1 = document.getElementsByName("languages");
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
console.log("dfghjk");
showsuggettiondiv();
document.getElementById("addmovielistbtn").onclick=function(){
    addmovietolist(id,per1,name);
}
}

function showsuggettiondiv(){
    console.log("dfghjk")
    var div = document.getElementById("suggetionbox");
    div.innerHTML="";
    div.style="display:flex";
    var arr = Object.keys(show);
    console.log(arr)
    arr.sort();
    console.log(arr);
    arr.forEach(key=>{
        var date = document.createElement("div");
        date.innerHTML=key.split('-').reverse().join('-');
        date.className="datediv";
        div.appendChild(date);
        date.onclick=function(){
            showsufficientscreens(show[key]);
        }
        
    })
}
function showsufficientscreens(json){
    var box = document.createElement("div");
    box.id="suggetionbox";
    box.style="z-index: 75;display:flex;overflow:scroll";
    var inp = document.createElement("input");
    inp.value="BACK";
    inp.type="button";
    inp.id="deletebtn"
    inp.className="inputbtn";
    inp.onclick=(e)=>{
        var btn = e.target;
        btn.parentElement.parentElement.removeChild(btn.parentElement);
        // console.log(e.target.parent.parent)
    }
    box.appendChild(inp)
    document.body.appendChild(box);
    Object.keys(json).forEach(key=>{
        var d = document.createElement("div");
            if(json[key]<=7){
                d.innerHTML=`1 screen capacity of ${key} for ${json[key]} show per day`
            }else{
                var screencount = Math.floor(json[key]/7);
                var balance = json[key]%7;
                d.innerHTML=`${screencount} screen capacity of ${key} for 7 show per day`
                if(balance!=0){
                    var d1 = document.createElement("div");
                    d1.innerHTML=`1 screen capacity of ${key} for ${balance} show per day`
                    d1.className="datediv";
                    d1.style="width:90%";
                    box.appendChild(d1);
                }
            }
        d.style="width:90%";
        d.className="datediv"
        box.appendChild(d);
    })
    
}
function hidethismovieform(btn,n){
    if(n==2){
        btn.parentElement.style=`display:none`;
        return;
    }
btn.parentElement.parentElement.style=`display:none`;
if(n==1){
    buylist=[];
}
if(n==0){
    document.getElementById("suggetionbox").style="display:none";
    document.getElementById("deletebtn").click();
    
}
}
var buylist=[];
function checkscreen(){
    var screencount = document.getElementById("screencount").value;
    var capacity = document.getElementById("capacity").value;
    document.getElementById("textofscreencount").style="height:auto;margin:0px;display:block"
    if(screencount != "" && capacity != ""){
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function(){
        if(this.readyState==4 && this.status == 200){
            var json = JSON.parse(this.responseText);
                if(json.statusCode==200){
                    json = JSON.parse(json.count);
                    if(json.count<screencount){
                        if(json.count>0){
                            document.getElementById("textofscreencount").innerHTML="you have only " +json.count+" screen below capacity of "+capacity;
                        }else{
                            document.getElementById("textofscreencount").innerHTML="you don't have any screen below capacity of "+capacity;
                        }
                        
                    }else{
                        document.getElementById("textofscreencount").innerHTML="";
                    }
                }else if(json.statusCode==401){
                    window.location.href="home.html";
                }else{
                    console.error(json);
                }            
        }
        }
        xhr.open("GET","./cinimas/impresario/checkscreen?cap="+capacity);
        xhr.setRequestHeader("Content-type","application/json");
        xhr.send();
    }
}


function addmovietolist(id,per1,name){
    var details={};
    details.per1=per1;
    details.movieId=id;
    details.screencount = document.getElementById("screencount").value;
    details.capacity = document.getElementById("capacity").value;
    details.showperday = document.getElementById("showperday").value;
    details.startday = document.getElementById("startday").value;
    details.endday = document.getElementById("endday").value;
    details.lId= "";
    for(let radio of document.getElementsByName("languages")){
        if(radio.checked){
            details.lan=radio.parentElement.parentElement.children[0].innerHTML;
            details.lId=radio.value;
            break;
        }
    }
    var val = true;
    Object.keys(details).forEach(k=>{
        if(details[k]=="" || details[k]==0){
            val=false;
        }
    })
    var date = new Date();
    var d1 = new Date(details.startday);
    var d2 = new Date(details.endday);
    console.log((d2-d1)/(1000*60*60*24))
    if(d2-d1 < 0 || d1 - date < 0 || (d2-d1)/(1000*60*60*24) > 5 || details.showperday>7){
        val=false;
        
    }
    if(val){
        var dates = [];
        for(let i=d1.getTime();i<d2.getTime();i+=86400000){
            const date = new Date(i);

const year = date.getFullYear();
const month = String(date.getMonth() + 1).padStart(2, '0');
const day = String(date.getDate()).padStart(2, '0');

const formattedDate = `${year}-${month}-${day}`;
dates.push(formattedDate);


            
        }
        dates.forEach(key=>{
            console.log( show[key][details.capacity])
            show[key][details.capacity] = show[key][details.capacity]-(details.screencount * details.showperday);
            if(show[key][details.capacity]<=0){
                delete show[key][details.capacity];
            }
        })
        console.log(show);
    }
    console.log(val);
    if(val){
        var box = document.getElementById("listofbuyedmovie");
        var div = document.createElement("div");
        var p1 = document.createElement("span");
        p1.innerHTML=name;
        div.appendChild(p1);

        var p2 = document.createElement("span");
        p2.innerHTML=details.screencount;
        div.appendChild(p2);

        var p3 = document.createElement("span");
        p3.innerHTML=details.capacity;
        div.appendChild(p3);

        var p4 = document.createElement("span");
        p4.innerHTML=details.showperday;
        div.appendChild(p4);

        var p5 = document.createElement("span");
        p5.innerHTML=details.startday;
        div.appendChild(p5);

        var p6 = document.createElement("span");
        p6.innerHTML=details.endday;
        div.appendChild(p6);
console.log(details.lan);
        var p7 = document.createElement("span");
        p7.innerHTML=details.lan;
        var input = document.createElement("input");
        input.value = "X";
        input.type="button";
        input.className="inputbtn";
        input.onclick=function(){
            deletethis(this);
        }
        div.appendChild(input)
        div.appendChild(p7);
box.appendChild(div);
        buylist.push(details);
        console.log(buylist);
        document.getElementById("screencount").value="";
    document.getElementById("capacity").value="";
    document.getElementById("showperday").value="";
    document.getElementById("startday").value="";
    document.getElementById("endday").value="";
    details={};
        for(let radio of document.getElementsByName("languages")){
            radio.checked=false;
        }
        document.getElementById("suggetionbox").style="display:none";
    document.getElementById("buymovieformdiv").style="display:none";
    document.getElementById("deletebtn").click();
}
}

function signout(){
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(){
    if(this.readyState==4 && this.status == 200){
        var json = JSON.parse(this.responseText);
            if(json.statusCode==200){
                window.location.href="customer.html";
            }else if(json.statusCode==401){
                window.location.href="customer.html";
            }else{
                console.error(json);
            }            
    }
    }
    xhr.open("GET","./cinimas/Signout");
    xhr.setRequestHeader("Content-type","application/json");
    xhr.send();
   
}

function deletethis(btn){
    var parent= btn.parentElement;
    var p = parent.parentElement;
    var i = Array.from(p.children).indexOf(parent) -2;
    var obj = buylist[i];
    var dates=[];
    var d1 = new Date(obj.startday);
    var d2 = new Date(obj.endday);
    for(let i=d1.getTime();i<d2.getTime();i+=86400000){
        const date = new Date(i);

const year = date.getFullYear();
const month = String(date.getMonth() + 1).padStart(2, '0');
const day = String(date.getDate()).padStart(2, '0');

const formattedDate = `${year}-${month}-${day}`;
dates.push(formattedDate);   
    }
    console.log(dates);
    dates.forEach(key=>{
        console.log(show[key][obj.capacity])
        if(show[key][obj.capacity]==undefined){
            show[key][obj.capacity]=0; 
        }
        show[key][obj.capacity]=show[key][obj.capacity]+(obj.screencount * obj.showperday)
        console.log(show[key][obj.capacity])
    })
    buylist.splice(i,1);
    p.removeChild(parent);
console.log(show);
}

function buyallmovie(btn){
    if(buylist.length>0){
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function(){
        if(this.readyState==4 && this.status == 200){
            var json = JSON.parse(this.responseText);
                if(json.statusCode==200){
                    hidethismovieform(btn,1);
                }else if(json.statusCode==401){
                    window.location.href="home.html";
                }else{
                    console.error(json);
                }            
        }
        }
        xhr.open("POST","./cinimas/impresario/addmovieforimpresario");
        xhr.setRequestHeader("Content-type","application/json");
        xhr.send(JSON.stringify(buylist));
    }
}

function addshowdiv(){
    var mhr = new XMLHttpRequest();
    mhr.onreadystatechange = function(){
        if(this.readyState==4 && this.status == 200){
            var json = JSON.parse(this.responseText);
            if(json.statusCode==200){
                    showalltheatreforshow(json.jsonarr);               
            }else if(json.statusCode==401){
                window.location.href="home.html";
            }else{
                console.error(json)
            }
        }
    }
    mhr.open("get","./cinimas/impresario/MyTheatres");
    mhr.setRequestHeader("Content-type","application/json");
    mhr.send();
}


function showalltheatreforshow(jsonarr){
    var arr=[];
    var box = document.createElement("div");
    arr.push(box);
    document.body.appendChild(box);
    box.id="theatreshowdiv";
    box.className="asdfghjkl"
    for(let json of jsonarr){
        var div = document.createElement("div");
        div.id="theatrebox";
        var span1 = document.createElement("span");
        span1.innerHTML=json.name;
        var span2 = document.createElement("span");
        span2.innerHTML=json.city;
        var span3 = document.createElement("span");
        span3.innerHTML=json.rating;
        div.appendChild(span1);
        div.appendChild(span2);
        div.appendChild(span3);
        div.onclick=(e)=>{
            selectedtheatre(json);
        }
        box.appendChild(div);
    }
    var input = document.createElement("input");
    input.value="cancel";
    input.id="theatrebox";
    input.className="inputbtn";
    input.type="button";
    input.onclick=(e)=>{
        var btn = e.target.parentElement;
        btn.parentElement.removeChild(btn);
    }
    box.appendChild(input);
}

function selectedtheatre(json){
    document.cookie="thId="+json.thId;
    currentth=json.thId;
    var mhr = new XMLHttpRequest();
        mhr.onreadystatechange = function(){
            if(this.readyState==4 && this.status == 200){
                var json = JSON.parse(this.responseText);
                if(json.statusCode==200){
                    if(json.jsonarr.length>0){
                        showallscreenforshow(json.jsonarr);
                    }                
                }else if(json.statusCode==401){
                    window.location.href="home.html";
                }else{
                    console.error(json)
                }
            }
        }
        mhr.open("get","./cinimas/impresario/fetchscreens?count=0"+"&thId="+currentth);
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send();
}

function showallscreenforshow(jsonarr){
    var box = document.createElement("div");
    document.body.appendChild(box);
    box.id="theatreshowdiv";
    box.className="asdfghjkl"
    box.style="z-index:102";
    for(let json of jsonarr){
        var div = document.createElement("div");
        div.id="theatrebox";
        var span1 = document.createElement("span");
        span1.innerHTML=json.name;
        var span2 = document.createElement("span");
        span2.innerHTML=json.sound;
        var span3 = document.createElement("span");
        span3.innerHTML=json.resolutions;
        div.appendChild(span1);
        div.appendChild(span2);
        div.appendChild(span3);
        div.onclick=()=>{
            selectedscreen(json);
        }
        box.appendChild(div);
    }
    var input = document.createElement("input");
    input.value="cancel";
    input.id="theatrebox";
    input.className="inputbtn";
    input.type="button";
    input.onclick=(e)=>{
        var btn = e.target.parentElement;
        btn.parentElement.removeChild(btn);
    }
    box.appendChild(input);
}

function selectedscreen(json){
    var mhr = new XMLHttpRequest();
    var scId = json.scId;
        mhr.onreadystatechange = function(){
            if(this.readyState==4 && this.status == 200){
                var json1 = JSON.parse(this.responseText);
                if(json1.statusCode==200){
                    getdatetime(scId,json1.count);                
                }else if(json1.statusCode==401){
                    window.location.href="home.html";
                }else{
                    console.error(json1)
                }
            }
        }
        mhr.open("get","./cinimas/impresario/fetchseatcount?thId="+currentth+"&scId="+json.scId);
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send();
}

function getdatetime(scId,count){
    var box = document.createElement("div");

    document.body.appendChild(box);
    box.id="theatreshowdiv";
    box.className="flexbox asdfghjkl"
    box.style="z-index:105";

    var input=document.createElement("input");
    input.type="date";
    input.className="inputbtn";
    input.onchange=(e)=>{
        while (box.childNodes.length > 1) {
            box.removeChild(box.lastChild);
          }
          console.log("getshowtime1042")
          const today = new Date();
const inputDate = new Date(input.value);
const differenceInMilliseconds = inputDate.getTime() - today.getTime();

// Convert the difference from milliseconds to days
const differenceInDays = differenceInMilliseconds / (1000 * 60 * 60 * 24);

// Check if the difference is greater than 0 and less than or equal to 5
if (differenceInDays > 0 && differenceInDays <= 5) {
  
        getshowtime(scId,count,input.value,box);  
        console.log(differenceInDays+"--------")
    }else{
        console.log(differenceInDays)
    }
                 
    }
    box.appendChild(input);
}

function getshowtime(scId,count,date,box){
    var div = document.createElement("div");
    div.className="fghj asdfghjkl"
    var mhr = new XMLHttpRequest();
        mhr.onreadystatechange = function(){
            if(this.readyState==4 && this.status == 200){
                var js = JSON.parse(this.responseText);
                if(js.statusCode==200){
                    for(let jn of js.jsonarr){
                        var div1 = document.createElement("div");
                        div1.innerHTML=`<span>${jn.time}</span>`;
                        div1.className="font"
                        var div2 = document.createElement("div");
                        div2.className="radiobtn boxroundshadow";

                        var input = document.createElement("input");
                        input.type="radio";
                        input.name="showtime";
                        input.value=jn.value;

        var span = document.createElement("span");

        div2.appendChild(input);
        div2.appendChild(span);
        div1.appendChild(div2);
        div.appendChild(div1);
                    } 
                    box.appendChild(div);
                    var radioset1 = document.getElementsByName("showtime");
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
                
                var input =  document.createElement("input");
                input.type="button";
                input.className="inputbtn";
                input.value="select Movie ->";
                input.onclick=()=>{
                    var val=false;
                    var showtime;
                    for(let radio of document.getElementsByName("showtime")){
                        if(radio.checked){
                            val=true;
                            showtime=radio.value;
                            break;
                        }
                    }
                    if(val){
                        var mhr = new XMLHttpRequest();
        mhr.onreadystatechange = function(){
            if(this.readyState==4 && this.status == 200){
                var json = JSON.parse(this.responseText);
                if(json.statusCode==200){
                    selectmovieforshow(json.jsonarr,scId,showtime,count,date);           
                }else if(json.statusCode==401){
                    window.location.href="home.html";
                }else{
                    console.error(json)
                }
            }
        }
        mhr.open("get","./cinimas/impresario/fetchmoviesforshow?thId="+currentth+"&scId="+scId+"&date="+date+"&count="+count);
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send();
                    }
                }
                box.appendChild(input);
                }else if(js.statusCode==401){
                    window.location.href="home.html";
                }else{
                    console.error(js)
                }
            }
        }
        mhr.open("get","./cinimas/impresario/fetchshowtime?thId="+currentth+"&scId="+scId+"&date="+date);
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send();
}


function selectmovieforshow(jsonarr,scId,showtime,count,date){
    var div = document.createElement("div");
    // div.className="asdfghjkl"
    document.body.appendChild(div);

    div.id="theatreshowdiv";
    div.className="flexsq asdfghjkl"
    div.style="z-index:110;    height: 80%;display:flex;flex-flow:row wrap;    width: 70%;";
    for(let json of jsonarr){
        var box = document.createElement("div");
        box.id="impremoviebuydiv";
        
        // var img = document.createElement("img");
        // img.id="buymovieimg";
        // img.src=json.smallpath;



        var img = document.createElement("div");
        // img.id="buymovieimg";
        img.style=`background-image:url('${json.smallpath}');background-position: center center;background-size:contain;background-repeat:no-repeat;backdrop-filter: blur(15px);height:100%;width:100%`;
        // img.
       
        var img1 = document.createElement("div");
        // img1.id="buymovieimg";

        img1.style=`background-image:url('${json.smallpath}');background-position: center center;background-size:cover;height:100%;width:100%;    height: 90%;
        width: 90%;
        border-radius: 10px;
        box-shadow: -3px -3px 6px 0 rgba(255, 255, 255, 0.2), 6px 6px 12px 0 rgba(0,0,0,0.3);
        border: none;
        outline: none;`;
        img1.appendChild(img);
        img1.id="buymoviedeatilsimg";




        box.appendChild(img1);
        var p1 = document.createElement("p");
        p1.innerHTML=json.name+" ( "+json.ratings+" )"
        box.appendChild(p1);
        
        var input = document.createElement("input");
        input.value="Book";
        input.className="inputbtn";
        input.type="button";
        input.style="text-align:center;font-weight:bolder;color:rgba(255,255,255,0.3);border-radius:10px"
        input.onclick=function(){
            var js={};
            js.thId=currentth;
            js.scId=scId;
            js.stId=showtime;
            js.movieId=json.movieId;
            js.lId=json.language;
            console.log(date);
            js.showdate=date;
            js.capacity=count;
            console.log(js);
            // 
            gettherateofticket(js);
        }
        box.appendChild(input);
        div.appendChild(box);
    }
}

function gettherateofticket(jsondet){
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(){
        if(this.readyState==4 && this.status == 200){
            var json = JSON.parse(this.responseText);
                if(json.statusCode==200){
                    console.log(json);
                    jsondet.per=json.per;
                   getrateforeverycatogery(json.jsonarr,jsondet);
                }else if(json.statusCode==401){
                    window.location.href="home.html";
                }else{
                    console.error(json);
                }            
        }
    }
    xhr.open("GET","./cinimas/impresario/fetchscreenstructure?thId="+jsondet.thId+"&scId="+jsondet.scId+"&mvId="+jsondet.movieId);
    xhr.setRequestHeader("Content-type","application/json");
    xhr.send();
}

function getrateforeverycatogery(jsonarr,json){
    console.log(json)
    var div = document.createElement("div");
    // div.className="asdfghjkl"
    document.body.appendChild(div);
var ids = [];
    div.id="theatreshowdiv";
    div.className="flexsq asdfghjkl"
    div.style="z-index:110;    height: 80%;display:flex;flex-flow:row wrap;    width: 70%;";
    var h2 = document.createElement("h2");
    h2.style="font-size:2rem;width:100%;color:white;text-align:center";
    h2.innerHTML="you buy this movie for "+json.per+" rs per seat";
    div.appendChild(h2);
    for(let json of jsonarr){
        var box = document.createElement("div");
        box.id="ratediv";

        var p = document.createElement("p");
        p.innerHTML=json[0][0];
        var inp = document.createElement("input");
        inp.id=json[0][0];
        inp.className="addscreeninput";
        inp.type="number";
        var v=[];
v.push(json[0][0]);
v.push(json[0][1]);
v.push(json[0][2]);
ids.push(v);
        box.appendChild(p);
        box.appendChild(inp);
        div.appendChild(box);

    }
    var input = document.createElement("input");
        input.className="inputbtn";
        input.value = "book";
        input.type="button";
        input.style="text-align:center;font-weight:bolder;color:rgba(255,255,255,0.3);border-radius:10px;height:30px";
        input.onclick=()=>{
var l=[];
for(let id of ids){
    var val = document.getElementById(id[0]).value; 
    if(val!=0 && val != "" && val>json.per){
        var d =[];
        d.push(id[0]);
        d.push(id[1]);
        d.push(id[2]);
        d.push(val);
        l.push(d);
    }
}
if(l.length==ids.length){
    json.rate=l;
    bookmyshow(json);
    console.log(json);
}
        }
        div.appendChild(input);

}

function bookmyshow(json){
    var mhr = new XMLHttpRequest();
        mhr.onreadystatechange = function(){
            if(this.readyState==4 && this.status == 200){
                console.log(this.responseText);
                var json1 = JSON.parse(this.responseText);
                if(json1.statusCode==200){
                    console.log(json1);
                }else if(json1.statusCode==401){
                    window.location.href="home.html";
                }else{
                    console.error(json1)
                }
            }
        }
        mhr.open("POST","./cinimas/impresario/bookmyshow");
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send(JSON.stringify(json));
        closeallandaddshow();
}


function closeallandaddshow(){
    var a = document.getElementsByClassName("asdfghjkl")
    for(let div of a){
        console.log(div);
        div.parentElement.removeChild(div);
    }
    for(let i=0;i<2;i++){
        a = document.getElementById("theatreshowdiv");
    a.parentElement.removeChild(a);
    }
    // console.log(json);
}


function getshow(){
    var mhr = new XMLHttpRequest();
    mhr.onreadystatechange = function(){
        if(this.readyState==4 && this.status == 200){
            var json1 = JSON.parse(this.responseText);
            if(json1.statusCode==200){
                addshowstopanel(json1.shows);
            }else if(json1.statusCode==401){
                window.location.href="home.html";
            }else{
                console.error(json1)
            }
        }
    }
    mhr.open("get","./cinimas/impresario/fetchmyshow");
    mhr.setRequestHeader("Content-type","application/json");
    mhr.send();
}

function addshowstopanel(jsonarr){
    var box  = document.getElementById("bookedshows");
    for(let json of jsonarr){
        var div = document.createElement("div");
        div.id="theatrebox";
        var span1 = document.createElement("span");
        span1.innerHTML=json.name;
        var span2 = document.createElement("span");
        span2.innerHTML=json.showdate;
        var span3 = document.createElement("span");
        span3.innerHTML=json.time;
        div.appendChild(span1);
        div.appendChild(span2);
        div.appendChild(span3);
        box.appendChild(div);
    }
}
getshow();
