var lastClicked,
    fb_database = firebase.database(); //firebase db instance

var addParam = (Param) =>{
  deleteTabaleContent();
  let tbody = $('#param-rows');
  var ref = fb_database.ref(Param + '/');
  ref.on("value", function(snapshot) {
    let set = snapshot.val();
    let location;
      for(let val in set){
        if(Param == 'users'){
          location = set[val]['LatestLoc'].split(' ');
        }else{
          location = set[val].split(' ');
        }
        let lat = location[0];
        let lng = location[1];
        var tr = 	'<tr>'+
                  '<td>' + val + '</td>' +
                  '<td>' + lat + '</td>' +
                  '<td>' + lng + '</td>' +
                  '</tr>';
        tbody.append(tr);
      }
  $('#params').append(tbody);
    }, function (error) {
    console.log("Error: " + error.code);
  });
}

var deleteTabaleContent = ()=>{
  let tablereset = document.getElementById('params');
  let size = tablereset.rows.length;
  for (var i = size - 1; i > 0; i--) {
    tablereset.deleteRow(i);
  }
}

var setErrorText = (text)=>{
  $('#error_text').text(text);
}

// Admin panel buttons
window.onload = ()=>{

  // On admin map click
  document.getElementById('getMap').addEventListener('click',()=>{
    window.location.href = './adminMap.html';
  });
  // On BLE button click
  document.getElementById('BLE').addEventListener('click',()=>{
    setErrorText('');
    lastClicked = 'BLE';
    document.getElementsByClassName('tool')[0].innerText = 'BLE Mac Address'
    $(function() {
      addParam('BLE');
    });
  });

  // On QR button click
  document.getElementById('QR').addEventListener('click',()=>{
    setErrorText('');
    lastClicked = 'QR';
    document.getElementsByClassName('tool')[0].innerText = 'QR number'
    $(function() {
      addParam('QR');
    });
  });

  // On users click
  document.getElementById('USERS').addEventListener('click',()=>{
    setErrorText('');
    lastClicked = 'users';
    document.getElementsByClassName('tool')[0].innerText = 'USER uid'
    $(function() {
      addParam('users');
    });
  });

  // On Add click
  document.getElementById('add').addEventListener('click',()=>{
    if(lastClicked == 'users'){
      $('#id_form').val('');
      $('#lat_form').val('');
      $('#lng_form').val('');
      $('#error_text').text('Adding a new user allowed only via registration!');
      $('#error_text').css('color', 'red');
    } else{
      let id = $('#id_form').val();
      let lat = $('#lat_form').val();
      let lng = $('#lng_form').val();
      
      fb_database.ref(lastClicked + '/' + id).set(
      lat + ' ' + lng
      );
    
      addParam(lastClicked);
      setErrorText('Added new ' + lastClicked + ' succeed!');
      $('#error_text').css('color', 'black');
      $('#id_form').val('');
      $('#lat_form').val('');
      $('#lng_form').val('');
    }
  });

} // End onload