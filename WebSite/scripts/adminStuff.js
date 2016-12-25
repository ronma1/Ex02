
// Admin panel buttons
window.onload = ()=>{

  // On admin map click
  document.getElementById('getMap').addEventListener('click',()=>{
    window.location.href = './adminMap.html';
  });

  // On BLE button click
  document.getElementById('BLE').addEventListener('click',()=>{
    document.getElementsByClassName('tool')[0].innerText = 'BLE Mac Address'
    $(function() {
      $.fn.addParam = function(Param){
        var ref = firebase.database().ref(Param + '/');
        ref.on("value", function(snapshot) {
          let bles = snapshot.val();
            for(let ble in bles){
              let location = bles[ble].split(' ');
              let lat = location[0];
              let lng = location[1];
              var tr = 	'<tr>'+
                        '<td>' + ble + '</td>' +
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
      var tbody = $('#param-rows');
      let tablereset = document.getElementById('params');
      let size = tablereset.rows.length;
      for (var i = size - 1; i > 0; i--) {
        tablereset.deleteRow(i);
      }
      tbody.addParam('BLE');
    });
  });

  // On QR button click
  document.getElementById('QR').addEventListener('click',()=>{
    document.getElementsByClassName('tool')[0].innerText = 'QR number'
    $(function() {
      $.fn.addParam = function(Param){
        var ref = firebase.database().ref(Param + '/');
        ref.on("value", function(snapshot) {
          let qrs = snapshot.val();
            for(let qr in qrs){
              let location = qrs[qr].split(' ');
              let lat = location[0];
              let lng = location[1];
              var tr = 	'<tr>'+
                        '<td>' + qr + '</td>' +
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

      var tbody = $('#param-rows');
      let tablereset = document.getElementById('params');
      let size = tablereset.rows.length;
      for (var i = size - 1; i > 0; i--) {
        tablereset.deleteRow(i);
      }
      tbody.addParam('QR');
    });
  });

  // On users click
  // On QR button click
  document.getElementById('USERS').addEventListener('click',()=>{
    document.getElementsByClassName('tool')[0].innerText = 'USER uid'
    $(function() {
      $.fn.addParam = function(Param){
        var ref = firebase.database().ref(Param + '/');
        ref.on("value", function(snapshot) {
          let qrs = snapshot.val();
            for(let qr in qrs){
              let location = qrs[qr]['LatestLoc'].split(' ');
              let lat = location[0];
              let lng = location[1];
              var tr = 	'<tr>'+
                        '<td>' + qr + '</td>' +
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

      var tbody = $('#param-rows');
      let tablereset = document.getElementById('params');
      let size = tablereset.rows.length;
      for (var i = size - 1; i > 0; i--) {
        tablereset.deleteRow(i);
      }
      tbody.addParam('users');
    });
  });
}