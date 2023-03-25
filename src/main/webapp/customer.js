var user=null;
var lat=null;
var lon=null;
var cookies = document.cookie.split("; ");
for(let cookie of cookies){
    var keyval = cookie.split("=");
    if(keyval[0]=="sessionId"){
        user=keyval[1];
        break;
    }else if(keyval[0]=="lat"){
        lat=keyval[1];
    }else if(keyval[0]=="lon"){
        lon=keyval[1];
    }
}
console.log(user);
if(user!=null){
    getname();
}else{
    unknown();
}

function unknown(){
    var mhr = new XMLHttpRequest();
    mhr.onreadystatechange = function(){
            if(this.readyState==4 && this.status == 200){
                var json = JSON.parse(this.responseText);
                if(json.statusCode==200){
                    console.log(json);
                    // getnearbymovies();    
                }else if(json.statusCode==401){
                    window.location.href="home.html";
                }else{
                    console.error(json)
                }
            }
        }
        mhr.open("get","./Login?email=unknown@unknown.com&otp=0");
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send();
}
function getname(){
    getnearbymovies();
    var mhr = new XMLHttpRequest();
    mhr.onreadystatechange = function(){
            if(this.readyState==4 && this.status == 200){
                var json = JSON.parse(this.responseText);
                if(json.statusCode==200){
                    setname(json.name);
                    role(json.role);
                    
                }
            }
        }
        mhr.open("get","./cinimas/FindName");
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send();
}

function setname(name){
    // if(name!=0){
        var btn= document.getElementById("email");
                   if(btn!=null){
                    btn.value=name;
                    btn.type="button";
                    btn.className="sidepagebtn";
                    var sidepage = document.getElementById("sidepage");
                    if(sidepage!=null){
                        if(document.getElementById("loginbtn")!=null){
                    sidepage.removeChild(document.getElementById("loginbtn"));
                    }
                }
                    document.getElementById("logout").innerHTML=`<i class="fa-solid fa-right-from-bracket"></i>`;
                    document.getElementById("logout").style="display:block;";
                }
    
}

document.body.onscroll=()=>{
    // console.log(document.documentElement.scrollTop)
    document.getElementById("sidepage").style=`top:${document.documentElement.scrollTop}px;`
document.getElementById("black").style=`top:${document.documentElement.scrollTop}px;`
}



function genreselectbox(btn){
    console.log(btn);
    // btn.style=`height:300px;`;
    btn.style="height:300px;overflow:scroll;";
    btn.addEventListener('mouseleave',function(e){
    
        e.target.style=`height:30px;overflow:hidden;`;
    })
}




function hamburger(){
    console.log("ham")
    document.body.style="transform:translateX(-20%);overflow:hidden;";
    var px=document.getElementById("black").style.top;
    document.getElementById("black").style=`top:${px};display:block;`;
}
function cross(){
    document.getElementById("black").style="display:none;";
    document.body.style="transform:translateX(0%);overflow-x:hidden;";
    document.documentElement.scrollLeft=0;
}
function signuppage(){
    document.cookie="role=O";
    window.location.href="login.html";
}

var email;
function login(){
    console.log(email);
    email = document.getElementById("email").value;
    if(email.trim()==""){
        return;
    }else{
        email=email.trim();
    }

    console.log(email);
    var mhr = new XMLHttpRequest();
    mhr.onreadystatechange = function(){
            if(this.readyState==4 && this.status == 200){
                var json = JSON.parse(this.responseText);
                console.log(this.responseText+"--------------------------------------------------");
                if(json.statusCode==200){

                    var div = document.createElement("div");
                    div.style=`position:absolute;height:100%;width:100%;top:${document.documentElement.scrollTop}px;left:0px;background-color:rgba(0,0,0,0.5);display:flex;justify-content:center;align-items:center;`;
                    var h1 = document.createElement("h1");
                    h1.innerHTML="otp is successfully sended to you email"
                    h1.style=`margin:0px;width:auto;padding:100px;
                    z-index: 200;
                    border-radius: 10px;
                    color: white;
                    background: #43ab43;
                `;
                    div.appendChild(h1);
                    document.body.appendChild(div);
                    document.getElementById("email").value="";
                    document.getElementById("email").placeholder="otp";

                    document.getElementById("loginbtn").value="Enter otp";
                    document.getElementById("loginbtn").onclick=function(){
                        var otp = document.getElementById("email").value;
                        if(otp.trim()!=""){
                            checkotp(email,otp);
                        }                        
                    }
                    setTimeout(function(){
                        document.body.removeChild(div);
                    },3000);
                }else if(json.statusCode==401){
                    window.location.href="customer.html";
                }else{
                    console.error(json)
                }
            }
        }
        var det = {};
        det.email=email;
        mhr.open("POST","./Login");
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send(JSON.stringify(det));
}


function  checkotp(email,otp){
    var mhr = new XMLHttpRequest();
    mhr.onreadystatechange = function(){
            if(this.readyState==4 && this.status == 200){
                var json = JSON.parse(this.responseText);
                if(json.statusCode==200){
                    setname(json.name);
                    role(json.role);
                }else if(json.statusCode==401){
                    window.location.href="customer.html";
                }else{
                    console.error(json);
                }
            }
        }
        mhr.open("get","./Login?email="+email+"&otp="+otp);
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send();
}

function role(role){
    var sidepage =   document.getElementById("sidepage");
    if(sidepage!=null){
        if(role=="D"){
            var inp1 = document.createElement("input");
            inp1.className="sidepagebtn";
            inp1.type="button"
            inp1.value="My Movies";
            inp1.onclick=function(){
                window.location.href="mymovies.html";
            }
            sidepage.appendChild(inp1);
        
            var inp3 = document.createElement("input");
            inp3.className="sidepagebtn";
            inp3.value="My Balanace";
            inp3.type="button";
        
            sidepage.appendChild(inp3);
            document.getElementById("joinimpresariobox").style="display:none;"
            document.getElementById("posterdiv").style="margin-bottom:100px;"
           }else if(role=="O"){
                var inp1 = document.createElement("input");
            inp1.className="sidepagebtn";
            inp1.type="button"
            inp1.value="Home";
            inp1.onclick=function(){
                window.location.href="impresario.html";
            }
            sidepage.appendChild(inp1);
            document.getElementById("posterdiv").style="margin-bottom:100px;"
            document.getElementById("joinimpresariobox").style="display:none;"
           }
    }
   
}

function signout(){
    var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function(){
            if(xhr.readyState==4 && xhr.status == 200){
                var i = xhr.responseText;
                console.log(i);
                var json = JSON.parse(i);
                console.log(json.Message);
                if(json.statusCode==200){
                    window.location.href="customer.html";
                }else{
                    console.error("unknown authorized session");
                }
            }
        }
        xhr.open("Get","./cinimas/Signout");
        xhr.setRequestHeader("Content-type","application/json");
        xhr.send();
}

function mymovies(){
    var mhr = new XMLHttpRequest();
    mhr.onreadystatechange = function(){
            if(this.readyState==4 && this.status == 200){
                var json = JSON.parse(this.responseText);
                console.log(json);
                if(json.statusCode==200){
                    addmoviestodistributor(json.jsonarr);
                }else if(json.statusCode==401){
                    window.location.href="customer.html";
                }else{
                    console.error(json);
                }
            }
        }
        mhr.open("get","./cinimas/distributor/DistributorMovies");
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send();
}


function getnearbymovies(){
    if ("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var latlon = {};
            latlon.latitude = position.coords.latitude;
            latlon.longitude = position.coords.longitude;
            console.log(latlon);
            var htp = new XMLHttpRequest();
            htp.onreadystatechange = function(){
                if(this.readyState==4 && this.status == 200){
                    var json = JSON.parse(this.responseText);
                    console.log(json)
                    if(json.statusCode==200){
                        console.log("poiuygfdfghjkhgfdsdfghjuujytgrfdsdfhyuujyhtgrfdsxdfghjhgfvdc")
                        var bo = document.getElementById("city")
                        if(bo!=null){
                            bo.innerHTML=json.loca;
                        }
                        
                        document.cookie="lat="+json.lat;
                        document.cookie="lon="+json.lon;
                        // fetchMovies();
                    }else if(json.statusCode==401){
                        unknown();
                    }else{
                        console.log(json)
                    }
                }       
            }
            htp.open("Post","./cinimas/LoadLocation");
            htp.setRequestHeader("Content-Type", "application/json");
            htp.send(JSON.stringify(latlon));
        });
        
    }else{
        console.error("Geolocation is not supported by this browser.")
    }
}

function fetchMovies(){
    var lat;
    var lon;
    for(let cookie of document.cookie.split("; ")){
        var aa = cookie.split("=");
        if(aa[0]=="lat"){
            lat=aa[1];
        }else if(aa[0]=="lon"){
            lon=aa[1];
        }
    }
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(){
        if(this.readyState==4 && this.status == 200){
            var i = this.responseText;
            var json = JSON.parse(i);
            if(json.statusCode==200){
                console.log(json.movie);
                console.log(json.img);
                showcasethemovie(json.movie,json.img);
            }else if(json.statusCode==401){
                window.location.href="home.html";
            }else{
                // document.getElementsByClassName("mnbvcxzxcvbnm")[0].innerHTML+="<h1>no movies found in your locations<h1>"
                // document.getElementById("lodpa").style="display:none";
                console.error(json);
                
            }
        }
    }
    xhr.open("POST","./cinimas/fetchmoviesdependonlocation");
    xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xhr.send("lat="+lat+"&lon="+lon);
}

var arrayofimage=[];

function showcasethemovie(jsonarr,arr){
    arrayofimage=arr;
    var box = document.getElementById("posterdiv");

    arr.forEach(k=>{
        var img = document.createElement("div");
        img.style=`
        height:100%;
        width:100%;
        background-image:url('${k}');
        background-size:contain;
        background-position:center center;
        backdrop-filter:blur(10px);
        `
        // box.appendChild(img);
    
        
        var img1 = document.createElement("div");
        img1.style=`
        height:100%;
        width:100%;
        background-image:url('${arr[2]}');
        background-size:cover;
        background-position:center center;
        background-repeat:no-repeat;`
        img1.appendChild(img);
        box.appendChild(img1);
    });

    jsonarr.forEach(films=>{
        var box  = document.getElementById("moviesdiv")
        


        var src =films.smallimg;
    var outerdiv = document.createElement("div");
    outerdiv.className="outerdiv";
    outerdiv.style=`background-image:url(${src});`;

    var innerdiv = document.createElement("div");
    innerdiv.className="innerdiv";
    innerdiv.style=`background-image:url(${src});`;

    outerdiv.appendChild(innerdiv);

 var name = document.createElement("p");
        name.className="text";
        name.innerText=films.name;
        

var div = document.createElement("div");
        div.style=`
           height: 100%;
    width: 100%;
    display: flex;
    justify-content: flex-end;
    align-items: flex-start;
    flex-flow: column wrap;
    position: relative;
    transition: all 0.5s ease 0s;
        `;
        div.appendChild(name);
        // div.appendChild(langen);
        outerdiv.appendChild(div);


        div.onclick=(e)=>{
            console.log(e.target);
            findscreenwiththismovie(films.movieId,films.lId);
        }
        box.appendChild(outerdiv);
    })



    setInterval(function(){
        var temp = document.getElementById("posterdiv").firstElementChild;
        document.getElementById("posterdiv").removeChild(temp);
        document.getElementById("posterdiv").appendChild(temp);
    },5000);
}
function findscreenwiththismovie(movieId,lId){
    console.log(movieId);
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(){
        if(this.readyState==4 && this.status == 200){
            var i = this.responseText;
            console.log(i);
            var json = JSON.parse(i);
            if(json.statusCode==200){
                console.log(json)
                json.movie.movieId=movieId;
                json.movie.lId=lId;
showallscreenwithmovie(json.movie,json.list);
            }else if(json.statusCode=401){
                console.log(json);
                // window.location.href="home.html";
            }
        }
    }
    xhr.open("Get","./cinimas/allscreenwiththismovie?movieId="+movieId+"&lId="+lId);
    xhr.setRequestHeader("Content-type","application/json");
    xhr.send();
}










function showallscreenwithmovie(movie,jsarr){
    console.log("----------------------------")
    console.log(jsarr);
    var box = document.createElement("div");
    box.style=`
    min-height:100%;
    width:100%;
    position:absolute;
    top:0px;
    
    backdrop-filter: blur(60px);
    background :rgba(255,255,255,0.2);
    
    `;
    var div1 = document.createElement("img");
    div1.style=`
    height:500px;
    width:100%;
    z-index:5;
    `;
    var div2 = document.createElement("div");
    div2.style=`
    height:500px;
    width:100%;
    z-index:10;
    position:absolute;
    top:0px;
    backdrop-filter: blur(10px);
    display:flex;
    justify-content:space-evenly;
    align-items:center;
    `;
    div1.src=movie.bigsrc;
    box.appendChild(div1)

var div3 = document.createElement("div");
div3.style=`
height:90%;
width:350px;
display:flex;
justify-content: center;
align-items: center;
// background:white;
box-shadow: 
    12px 12px 16px 0 rgba(111,111, 111, 0.25),
    -8px -8px 12px 0 rgba(255, 255, 255, 0.3);
`;

var div4 = document.createElement("img");
div4.style=`
height:auto;
width:95%;
background-filter:blur(10px);
background:rgba(0,0,0,0.5);
`;
div4.src=movie.bigsrc;
div3.appendChild(div4);

var div5 = document.createElement("div");
div5.id="lkjhgfghjk"
div5.style=`
height:80%;
width:60%;
backdrop-filter: blur(10px);
    background :rgba(0,0,0,0.2);
    color:white;
    position:relative;
    padding-left:  10%;
    padding-top:50px;
    text-align:left;
`;
var gen = "";
for(let s of movie.genres){
    gen+=s+" | "
}
div5.innerHTML=`
<h1>${movie.name+" ( "+movie.rating+" ) - "+movie.language}</h1><br>
<p>${movie.language+" | "+movie.rating}</p>
<p>${gen+movie.date+" | "+movie.totaltime}</p>
<p>Actors   : ${movie.cast}</p>
<p>Director : ${movie.director}</p>
<p>Music Director : ${movie.musicdirector}</p>
<h2 style="margin-top:10px">Synopsis</h2>
<p style="width:450px">${movie.sysnopsis}</p>
`;

var inpu = document.createElement("input");
inpu.type="button";
inpu.id="clickme";
inpu.className="inputbtn";
inpu.value="Back";
inpu.onclick=function(e){
    selectedseats=[];
    var btn = e.target.parentElement.parentElement;
    btn.parentElement.removeChild(btn);
    btn = document.getElementsByClassName("sdfghjkl")[0];
    btn.parentElement.removeChild(btn);
  
    btn = document.getElementById("lkbv");
btn.parentElement.removeChild(btn);



}
div2.appendChild(inpu);

div2.appendChild(div3);
div2.appendChild(div5)



    box.appendChild(div2)

var div6 = document.createElement("div");
div6.style=`
min-height:${document.body.scrollHeight-500}px;
width:75%;
margin:auto;
backdrop-filter: blur(10px);
    background :rgba(0,0,0,0.5);
    color:white;
`;
var d = new Date(jsarr[0].showdate);
const daysOfWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
div6.innerHTML=`<h1>${d.getDate()+" \n"+daysOfWeek[d.getDay()]}</h1>`
for(let json of  jsarr){
    var div8 = document.createElement("div");
    div8.id="lkjhgfghjk";
    div8.style=`
    height:250px;
    text-align:center;
    width:100%;
    border-bottom:1px solid rgba(0,0,0,0.3);
    display:flex;
    justify-content:space-between;
    align-items: center;
    `;
    var div9 = document.createElement("div");
    div9.style=`
    height:100%;
    width:35%;
    display:flex;
    padding-left:10%;
    margin-right:25px;
    justify-content: center;
    align-items: flex-start;
    flex-direction:column;
    `;
    div9.innerHTML=`
    <h2>${json.name}</h2>
    <p>${json.address}</p>`;
    div8.appendChild(div9);
    div6.appendChild(div8);

    var div10 = document.createElement("div");
    div10.style=`
    height:100%;
    width:60%;
    display:flex;
    justify-content:space-evenly;
    align-items: center;
    `;
    for(let time of json.showtime){
        var span = document.createElement("span");
    span.innerHTML=time[1].toUpperCase();
    span.onclick=function(){
        var js={...json};
        js.showtime=time[0];
        js.movie=movie;
        console.log(js);
        getseats(js);
        
    }
    div10.appendChild(span);
    }
    div8.appendChild(div10);
}
box.appendChild(div6);

    document.body.appendChild(box);

}


function getseats(json1){
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(){
        if(this.readyState==4 && this.status == 200){
            var i = this.responseText;
            console.log(i);
            var json = JSON.parse(i);
            if(json.statusCode==200){
                console.log(json);
                showseatstoselect(json.arr,json1);
            }else if(json.statusCode=401){
                console.log(json);
                // window.location.href="home.html";
            }
        }
    }
    xhr.open("post","./cinimas/fetchseatsforshow")
    xhr.setRequestHeader("Content-type","application/json");
    xhr.send(JSON.stringify(json1));
}

function hidestructure(btn){
    btn.parentElement.removeChild(btn);
    document.body.style="overflow:unset";
}


function isuser(){
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(){
        if(this.readyState==4 && this.status == 200){
            return this.responseText;
        }
    }
    xhr.open("get","./cinimas/confirm",false);
    xhr.setRequestHeader("Content-type","application/json");
    xhr.send();
}

var amount = {};
var list = [];
function showseatstoselect(jsonarr,json){
    var id =  isuser();
    // seatofthis=jsonarr;
    // document.getElementById("cHeader").style="display:none";
    console.log(jsonarr);
    jsonarr.reverse();
    var div = document.createElement("div");
    document.body.appendChild(div);
    div.innerHTML=`<input type="button" class="inputbtn" id="hidescreenstructurebtn" onclick="hidestructure(this.parentElement)" value="Back">`;
div.style=`    display: block;
background: black;
overflow: scroll-Y;
height: 100%;
width: 100%;
position: absolute;
overflow-x:hidden;
overflow-y:scroll;
top:${document.documentElement.scrollTop}px;
z-index: 15;`;
document.body.style="overflow:hidden;"
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
        r.reverse();
        var st = r[0].rowno;
        var row = document.createElement("div");
        row.className="row";
        for(let seat of r){
            if(seat.rowno==st){
                var seatdiv =   document.createElement("div");
                seatdiv.className="seat";
                seatdiv.style=`z-index:${z};`;
                // seatdiv.id=seat.rowno;
                seatdiv.id=seat.zx+"-"+seat.groupid+"-"+seat.seId+"-"+seat.columnno+"-"+seat.rowno; 
                if(seat.status=="notseat"){
                    seatdiv.style="background:none";
                }else if(seat.status=="booked"){
                    seatdiv.className="saet";
                    seatdiv.style="background:grey";
                }
                else{
                    seatdiv.onclick=(e)=>{
                        e.target.classList.toggle("select");
                        console.log(e.target);
                    }
                }
                row.appendChild(seatdiv);
            }else{
                st=seat.rowno;
                row.style=`transform:translateY(${(z-20)/2*(-5)}px) scale(${1+(((z-20)/2)/30)});`;
                z+=2;
                cat.appendChild(row);
                
                row=document.createElement("div");
                row.className="row";
                var seatdiv = document.createElement("div");
                seatdiv.className="seat";
                seatdiv.id=seat.zx+"-"+seat.groupid+"-"+seat.seId+"-"+seat.columnno+"-"+seat.rowno; 
                seatdiv.style=`z-index:${z};`;
                if(seat.status=="notseat"){
                    seatdiv.style="background:none";
                }else{
                    seatdiv.onclick=(e)=>{
                        e.target.classList.toggle("select");
                        console.log(e.target);
                    }
                }
                row.appendChild(seatdiv);
            }
        }
        cat.appendChild(row);
        row.style=`transform:translateY(${(z-20)/2*(-5)}px) scale(${1+(((z-20)/2)/30)});`;
        var p = document.createElement("p");
        p.innerHTML=json[0][2] +"-"+json[0][1];
        p.style=`transform:translateY(${(z-20)/2*(-5)}px) scale(${1+(((z-20)/2)/30)});`;
        cat.appendChild(p);
        amount[json[0][0]]=json[0];
        div.appendChild(cat);
        

    }
    var inpu = document.createElement("input");
    inpu.className="inputbtn";
    
    inpu.value="BOOK";
    inpu.type="button"
    inpu.style="margin:auto;"
    inpu.onclick=(e)=>{
        if(id){
            amounta(false,null,json);
        }else{
            var inp = document.createElement("input");
            inp.type="text";
            inp.placeholder="Email";
            inp.style="color:white;width:300px;text-transform: none;";
            inp.className="inputbtn";
            // inp.style="width:200px;color:white;"
            mnbgh.appendChild(inp);
            e.target.onclick=()=>{
                if(inp.value.trim!=""){
                    amounta(true,inp.value.trim(),json);
                }
            }
        }
    }

    var mnbgh = document.createElement("div");
mnbgh.style=`height: 500px;
width: 1200px;
// background-image: url('MT.png');
// background: currentColor;
// background-position: center center;
// margin: auto;
// background:grey`
mnbgh.appendChild(inpu);
div.appendChild(mnbgh);
}


function amounta(bol,inp,json){
    if(bol){
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function(){
            if(this.readyState==4 && this.status == 200){
                // getname();
            }
        }
        xhr.open("post","./cinimas/confirm",false);
        xhr.setRequestHeader("Content-type","application/json");
        xhr.send("email="+inp);
    }
    var div = document.createElement("div");
    div.style=`
    height:100%;
    width:100%;
    position:absolute;
    top:${document.documentElement.scrollTop}px;
    backdrop-filter: blur(20px);
    display:flex;
    justify-content:   center;
    align-items:center;
    flex-direction:column;
    z-index:25;
    `;
    document.body.appendChild(div);
    document.body.style="overflow:hidden;";
    
    var rate = {};
    var cost = 0;
    for(let id of document.getElementsByClassName("select")){
        console.log(id);
        list.push(id.id);
        var c =id.id.split("-");
        var d = rate[c[3]];
        if(d==null){
            d=[0,0,amount[c[3]][2]];
        }
        d[0]+=amount[c[3]][1];
        cost+=amount[c[3]][1];
        d[1]++;
        rate[c[3]]=d;
    }
console.log(rate);
    var m = document.createElement("div");
    rate = Object.entries(rate);
    for(let r of rate){
        console.log(r)
        var p = document.createElement("p");
        p.style="color:white;font-weight:bolder;";
        p.innerHTML= r[1][2]+" X "+r[1][1]+" = "+r[1][0];
        div.appendChild(p);
    }
    var p = document.createElement("p");
    p.style="color:white;font-weight:bolder;";
        p.innerHTML="total"+"   = "+cost;
        div.appendChild(p);

        var l = document.createElement("input");
        l.type="text";
        l.className="inputbtn";
        l.placeholder="amount";
        l.style="color:white;width:300px;text-transform: none;";

        div.appendChild(l);

    var inp = document.createElement("input");
            inp.type="button";
            // inp.placeholder="Email";
            inp.className="inputbtn";
            inp.value="book"
            div.appendChild(inp);
            inp.onclick=(e)=>{
                console.log(e.target)
                if(l.value>=cost){
                    console.log(list);
                    console.log(json);
                    var js ={};
                    js.tickets=list;
                    js.details=json;

                    var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function(){
            if(this.readyState==4 && this.status == 200){
                window.location.href="customer.html"
            }
        }
        xhr.open("post","./cinimas/buyticket",false);
        xhr.setRequestHeader("Content-type","application/json");
        xhr.send(JSON.stringify(js));
                }
                
            }

}













function addmoviestodistributor(jsarr){
    var box = document.getElementById("distributormoviediv");
    for(let json of jsarr){
        var div = minimoviediv(json,1);
        box.appendChild(div);
    }
}

function addmovieframe(){
    window.location.href="addmovie.html";
}
var genreids=[];
var languageids=[];
var languagemap = {};
var genremap={};
async function addmoviepage(){

    var wholediv = document.createElement("div");
    wholediv.id="wholediv";
    
    document.getElementById("addgenrebtn").onclick=()=>{
        wholediv.style="display:flex";
        wholediv.firstElementChild.style="display:flex";
    }
    document.getElementById("addlanguagebtn").onclick=()=>{
        wholediv.style="display:flex";
        wholediv.firstElementChild.nextElementSibling.style="display:flex";
    }
    var poster = document.getElementById("bigposter");
    poster.onchange=async ()=>{
        showposter();
        var inp = document.createElement("input");
        inp.value="show poster";
        inp.type="button"
        inp.className="btnfordis";
        inp.onclick=()=>{showposter()};
        var syspos= document.getElementById("syspos");
        if(syspos.children.length==4){
            syspos.appendChild(inp)
        }
    }

    var genres= await fetchgenres();
    genres.forEach(k=>{
        genremap[k.id]=k.name;
    })
    // var genres=[];
    var genrestorage = document.getElementById("genrestorage");

    wholediv.appendChild(addassets(genres,genrestorage,genreids));

    // var Languages= await fetchLanguage();
    var Languages = await fetchlanguages();
    Languages.forEach(k=>{
        languagemap[k.id]=k.name;
    })
    var Languagestorage = document.getElementById("languagestorage");

    wholediv.appendChild(addassets(Languages,Languagestorage,languageids));
    document.body.appendChild(wholediv);
}

async function showposter(){
    var poster = document.getElementById("bigposter");
    wholediv.style="display:flex";
        var div = document.createElement("div");
        div.className="posterdiv";
        var base64 = await fileto64(poster.files[0]);
        console.log("dfghjk")
        div.style=`background-image:url(${base64});`;
        var img  = document.createElement("div");
        img.style=`background-image:url(${base64});`;
        img.className="miniposterdiv";
        div.appendChild(img);
        div.onclick=(e)=>{
            wholediv.removeChild(e.target.parentElement);
            wholediv.style="display:none";
            
        }
        wholediv.appendChild(div);
}

function addassets(jsarr,storage,arr){
    var div = document.createElement("div");
    div.className="centerdiv";

    var inp = document.createElement("input");
    inp.className="roundcross";
    inp.type="button";
    inp.value="X";
    inp.onclick=(e)=>{
        e.target.parentElement.style="display:none";
        e.target.parentElement.parentElement.style="display:none";
    }
    div.appendChild(inp);
    for(let json of jsarr){
        div.appendChild(outdiv(div,storage,json,arr));
    }
    return div;
}

function outdiv(div,storage,json,arr){
    var input = document.createElement("input");
        input.type="button";
        input.value=json.name;
        input.classList="btnfordis";
        input.onclick=function(e){
            var btn = e.target;
            btn.parentElement.removeChild(btn);
            arr.push(json.id);
            storage.appendChild(indiv(div,storage,json,arr,btn));
        }
    return input;
}

function indiv(div,storage,json,arr,outdiv){
    var inp = document.createElement("input");
    inp.type="button";
    inp.value=json.name;
    inp.classList="btnfordis";
    

    inp.onclick=function(ev){
        arr.splice(arr.indexOf(json.id),1);
        storage.removeChild(ev.target);
        div.appendChild(outdiv);
    }
    return inp;
}


function fetchgenres(){
    return new Promise(resolve => {
        var mhr = new XMLHttpRequest();
    mhr.onreadystatechange = ()=>{
            if(mhr.readyState==4 && mhr.status == 200){
                var json = JSON.parse(mhr.responseText);
                if(json.statusCode==200){
                   resolve(json.genre);
                }else if(json.statusCode==401){
                    window.location.href="customer.html";
                }else{
                    //  [];
                }
            }
        }
        mhr.open("get","./cinimas/distributor/FetchGenre");
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send();
      });
    
}


function fetchlanguages(){
    return new Promise(resolve => {
        var mhr = new XMLHttpRequest();
    mhr.onreadystatechange = ()=>{
            if(mhr.readyState==4 && mhr.status == 200){
                var json = JSON.parse(mhr.responseText);
                if(json.statusCode==200){
                   resolve(json.language);
                }else if(json.statusCode==401){
                    window.location.href="customer.html";
                }else{
                    //  [];
                }
            }
        }
        mhr.open("get","./cinimas/distributor/FetchLanguages");
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send();
      });
}


function fileto64(file){
    return new Promise(resolve => {
       if(file!=null){
        const reader = new FileReader();
    
        reader.addEventListener('load', function() {
            console.log(reader.result)
            resolve(reader.result)
        });
        
      
    reader.readAsDataURL(file);
       }else{
        resolve("");
       }
      });
    
}

var film;
var lmap={};
var gmap={};
var imgarr=[];
async function getimages(){

    if(await chechfill()){
        console.log(film);
        var wholediv = document.getElementById("wholediv");

        var box= document.createElement("div");
        box.className="centerdiv";
        box.style="display:flex";


        var inp = document.createElement("input");
        inp.className="btnfordis";
        inp.style="position:absolute;top:1%;right:2%;";
        inp.type="button";
        inp.value="Add Movie";
        inp.onclick=()=>{
            if(imgarr.length==languageids.length){
                film.imgarr=imgarr;
                wholediv.removeChild(box);
                addmovie(film);
            }
        }
        box.appendChild(inp);
        wholediv.appendChild(box);
        languageids.forEach(k=>{
            var div=document.createElement("div");
            var input = document.createElement("input");
            input.type="button";
            input.value=languagemap[k];
            input.className="btnfordis";

            var file= document.createElement("input");
            file.type="file";
            file.style="display:none";
            file.onchange= async ()=>{
                var base64 = await fileto64(file.files[0]);
                var img = {};
                img.src=base64;
                img.type=file.files[0].type;
                img.languageid=k;
                img.name=film.name;
                imgarr.push(img);
                file.previousElementSibling.style="border-color:green;color:green;";
                var photo = document.createElement("div");
                photo.style=`
                height:100%;
    width:100%;
    position :absolute;
    background:rgba(0,0,0,0.1);
    backdrop-filter:blur(10px);
    display:flex;
    justify-content: center;
    align-items: center;
    z-index: 25;
                `;
                var imageblur = document.createElement("div");
                imageblur.className="outerdiv";
                imageblur.style=`
                
    background-image:url(${base64});
                `;
                var image = document.createElement("div");
                image.className="innerdiv";
                image.style=`
               
    background-image:url(${base64});
 
                `;
                imageblur.appendChild(image);
                photo.appendChild(imageblur);
                imageblur.onclick=(e)=>{
                    document.body.removeChild(photo);
                }
                document.body.appendChild(photo);
            }
            
            div.appendChild(input);
            div.appendChild(file);
            div.onclick=(e)=>{
                console.log(e.target);
                if(e.target.nextElementSibling){
                    e.target.nextElementSibling.click();
                }else{
                    e.target.click();
                }
                
            }
            
            box.appendChild(div);
        })

        

        wholediv.style="display:flex";

    }
}


function home(){
    window.location.href="customer.html";
}


async function chechfill(){
    return new Promise(async resolve => {
        var movie = {};
    movie.name=document.getElementById("moviename").value.trim();
    movie.ratings=null;
    for(let rating of document.getElementsByName("ratings")){
        if(rating.checked){
            movie.ratings=rating.value;
        }
    }

    movie.experience=null;
    for(let experience of document.getElementsByName("experience")){
        if(experience.checked){
            movie.experience=experience.value;
        }
    }

    movie.hero=document.getElementById("hero").value.trim();
    movie.heroine=document.getElementById("heroine").value.trim();
    movie.villan=document.getElementById("villan").value.trim();
    movie.director=document.getElementById("director").value.trim();
    movie.musicdirector=document.getElementById("musicdirector").value.trim();
    genreids.forEach(k=>{
        gmap[k]=genremap[k];
    })
    movie.genre=gmap;
    languageids.forEach(k=>{
        lmap[k]=languagemap[k];
    })
    movie.language=lmap;
    movie.synopsis=document.getElementById("synopsis").innerHTML;
    var poster = document.getElementById("bigposter");
    console.log(poster.files[0]);
    var base64 = await fileto64(poster.files[0]);
    var img = {};
                img.src=base64;
                img.type=poster.files[0].type;
                img.name=poster.files[0].name;
    movie.bigimg =  img;
    movie.date=document.getElementById("date").value;
    movie.hours=document.getElementById("hours").value;
    movie.min=document.getElementById("mins").value;
    movie.baseprice=document.getElementById("baseprice").value;
    movie.capacity=document.getElementById("capacity").value;

    Object.keys(movie).forEach(key => {
            if(!movie[key] || movie[key]==null || movie[key]==undefined || movie[key]==0 || movie[key]==''|| movie[key]== []){
                resolve(false);
            }
          });
    console.log(movie)
    film=movie;
    resolve(true);
      });
}

function addmovie(film){
    film.purpose="Addmoviefordistributors";
    
    var mhr = new XMLHttpRequest();
    mhr.onreadystatechange = ()=>{
            if(mhr.readyState==4 && mhr.status == 200){
                console.log(this.responseText)
                var json = JSON.parse(mhr.responseText);
                if(json.statusCode==200){
                    window.location.href="mymovies.html";
                }else if(json.statusCode==401){
                    window.location.href="customer.html";
                }else{
                    //  [];
                }
            }
        }
        mhr.open("Post","./cinimas/distributor/AddMovie");
        mhr.setRequestHeader("Content-type","application/json");
        mhr.send(JSON.stringify(film));
   
}



function minimoviediv(films,num){
    var src =films.smallpath[0].src;
    var outerdiv = document.createElement("div");
    outerdiv.className="outerdiv";
    outerdiv.style=`background-image:url(${src});`;

    var innerdiv = document.createElement("div");
    innerdiv.className="innerdiv";
    innerdiv.style=`background-image:url(${src});`;

    outerdiv.appendChild(innerdiv);

 var name = document.createElement("p");
        name.className="text";
        name.innerText=films.name;
        var langen = document.createElement("p");
        langen.className="text";
        langen.style="margin:0px;margin-bottom:10px;";
var str="";
        Object.keys(films.language).forEach(k=>{
            str+=films.language[k]+" * "
        })
        str+="( "+films.ratings+" )";
        
            str+=" * "+films.genre[Object.keys(films.genre)[0]]
        
        langen.innerHTML=str;
var div = document.createElement("div");
        div.style=`
           height: 100%;
    width: 100%;
    display: flex;
    justify-content: flex-end;
    align-items: flex-start;
    flex-flow: column wrap;
    position: relative;
    transition: all 0.5s ease 0s;
        `;
        div.appendChild(name);
        div.appendChild(langen);
        outerdiv.appendChild(div);
if(num==1){
    return outerdiv;
}









        // var input =    document.createElement("input");
        // input.className="playbtn";
        // input.value="Play Trailer";
        // input.type="button";
        // div.appendChild(input);

        var inp =    document.createElement("input");
        inp.className="playbtn";
        inp.value="Buy Ticket";
        inp.type="button";
        div.appendChild(inp);


    return outerdiv;
        var outerdiv = document.createElement("div");
        outerdiv.className="imageblur";
        var src =films.smallpath[0].src;
        // outerdiv.addEventListener("mouseover",function(){
        //     console.log(1)
        //     outerdiv.firstElementChild.style= outerdiv.firstElementChild.style+";transform: translate(-50%,-50%) scale(1.3);";
        // })

        outerdiv.addEventListener("mouseover",function(e){
            e.target.style.transform = 'translate(-50%,-50%) scale(1.3)';
        });
        outerdiv.addEventListener("mouseleave",function(e){
            e.target.style.transform = ' scale(1)';
        });

        outerdiv.style=`
                
        background-image:url(${src});
                    `;
        var innerimg = document.createElement("div");
        innerimg.className="image";
        innerimg.style=`
                
        background-image:url(${src});
                    `;
                    outerdiv.appendChild(innerimg);
                    return outerdiv;
        var div = document.createElement("div");
        div.style=`height:100%;
        width:100%;
        display: flex;
    justify-content: flex-end;
    align-items: flex-start;
    flex-flow: row wrap;
        `;
       


   
    
}



