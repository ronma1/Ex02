var lastClicked;

var addParam = (Param) =>{
  let tbody = $('#param-rows');
  var ref = firebase.database().ref(Param + '/');
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

// Admin panel buttons
window.onload = ()=>{

  // On admin map click
  document.getElementById('getMap').addEventListener('click',()=>{
    window.location.href = './adminMap.html';
  });
  // On BLE button click
  document.getElementById('BLE').addEventListener('click',()=>{
    lastClicked = 'BLE';
    document.getElementsByClassName('tool')[0].innerText = 'BLE Mac Address'
    $(function() {
      deleteTabaleContent();
      addParam('BLE');
    });
  });

  // On QR button click
  document.getElementById('QR').addEventListener('click',()=>{
    lastClicked = 'QR';
    document.getElementsByClassName('tool')[0].innerText = 'QR number'
    $(function() {
      deleteTabaleContent();
      addParam('QR');
    });
  });

  // On users click
  // On QR button click
  document.getElementById('USERS').addEventListener('click',()=>{
    lastClicked = 'users';
    document.getElementsByClassName('tool')[0].innerText = 'USER uid'
    $(function() {
      deleteTabaleContent();
      addParam('users');
    });
  });

}