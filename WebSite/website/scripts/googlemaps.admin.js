
// globals
var map, // google map instance
    pos, // My position
    marker,
    markers;

// Note: This example requires that you consent to location sharing when
// prompted by your browser. If you see the error "The Geolocation service
// failed.", it means you probably did not give permission for the browser to
// locate you.
function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: -34.397, lng: 150.644},
    zoom: 6
  });

  // Note for myself: Do not set new infowindow unless you learn how to control there's timeout :) . 
  // var infoWindow = new google.maps.InfoWindow({map: map});

  // Try HTML5 geolocation.
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(position) {
      pos = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };

       marker = new google.maps.Marker({
          position: pos,
          map: map,
          icon: '../img/blue-dot.png'
        });


      // infoWindow.setPosition(pos);
      // infoWindow.setContent('Location found.');
      map.setCenter(pos);
    }, function() {
      handleLocationError(true, infoWindow, map.getCenter());
    });
  } else {
    // Browser doesn't support Geolocation
    handleLocationError(false, infoWindow, map.getCenter());
  }
}

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
  infoWindow.setPosition(pos);
  infoWindow.setContent(browserHasGeolocation ?
                        'Error: The Geolocation service failed.' :
                        'Error: Your browser doesn\'t support geolocation.');
}

// On windows load set buttons listeners 
window.onload = ()=>{
  document.getElementById('center_me').addEventListener("click", ()=>{
    map.setCenter(pos);
  });
  document.getElementById('zoom_in').addEventListener("click", ()=>{
    map.setZoom(map.getZoom() + 1);
  });
  document.getElementById('zoom_out').addEventListener("click", ()=>{
    map.setZoom(map.getZoom() - 1);
  });
  
}

