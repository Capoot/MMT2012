
$(document).ready(function (){

    App.init();
});

var App = {
    map: null,
    initialLocation: new google.maps.LatLng(52, 13),
    userLocation: null,
    cameras: {},
    infoWindow: null,

    init: function() {
        var myOptions = {
            zoom: 8,
            center: App.initialLocation,
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            disableDefaultUI: true,
            scaleControl: true,
            zoomControl: true,
            panControl: true
        };
        App.map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
        App.showUserOnMap();
        App.addCamera(123);
    },

    showUserOnMap: function() {
        if (!navigator.geolocation) {
            return;
        }
        navigator.geolocation.getCurrentPosition(function(position) {
            App.showPosition(position.coords.latitude, position.coords.longitude);
        });
    },

    showPosition: function(lat, lng) {
        var latlng = new google.maps.LatLng(lat, lng);
        App.userLocation = latlng;
        App.map.setCenter(latlng);
    },

    addCamera: function(id) {
        var marker = new google.maps.Marker({
            position: new google.maps.LatLng(52, 13),
            title:"Camera ###",
            map: App.map,
            icon: "img/video.png"
        });
        google.maps.event.addListener(marker, 'click', function() {
            var info = '<article><h1>LiveStream</h1>'
                + '<video autoplay="autoplay" width="270" height="480" controls="controls">'
                + '<source src="testData/video.webm" type="video/webm" />'
                + '<source src="testData/video.mp4" type="video/mp4" />'
                + '<source src="testData/video.webm" type="video/ogg; codecs=\'theora, vobis\'" />'
                + '</video></article>';
            var infowindow = new google.maps.InfoWindow({
                content: info,
                maxWidth: 500
            });
            infowindow.open(App.map, marker);
            App.infoWindow = infowindow;
        });
        App.cameras[id] = marker;
    }
}