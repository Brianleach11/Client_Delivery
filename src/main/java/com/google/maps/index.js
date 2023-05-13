let map;

function initMap() {
    // The location of Uluru
    const position = { lat: 26.0797377, lng: -80.27373039 };

    // The map, centered at Uluru
    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 4,
        center: position,
        mapId: "DEMO_MAP_ID",
    });

    const trafficLayer = new google.maps.TrafficLayer();

    trafficLayer.setMap(map);
}

async function getOptimizedRoute(addresses) {
    try {
        const response = await fetch("http://localhost:8080/optimize-route", {
            method: "POST",
            body: JSON.stringify({ strings: addresses }),
            headers: {
                "Content-type": "application/json"
            }
        });
        const json = await response.json();
        console.log(json);
        return json;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

async function handleSubmit(event) {
    // Prevent the default form submission behavior
    event.preventDefault();
    console.log("Handling the submit Button");

    // Get the value of the addresses input field
    const addresses = document.getElementById("inputAddresses").value.split(",");
    // Call getOptimizedRoute with the addresses value
    try {
        let jsonData = await getOptimizedRoute(addresses);
        const latLngList = Object.values(jsonData);

        let directionsService = new google.maps.DirectionsService();
        let directionsRenderer = new google.maps.DirectionsRenderer();
        directionsRenderer.setMap(map);

        let waypoints = latLngList.slice(1, latLngList.length - 1).map(({ lat, lng }) => ({
            location: { lat, lng },
            stopover: true
        }));

        let request = {
            origin: latLngList[0], // Start
            destination: latLngList[latLngList.length - 1], // End
            waypoints: waypoints, // Waypoints
            travelMode: 'DRIVING' // Travel mode
        };

        directionsService.route(request, function(result, status) {
            if (status == 'OK') {
                // Set the directions on the map
                directionsRenderer.setDirections(result);

                // Get the route's bounds
                var bounds = new google.maps.LatLngBounds();
                var route = result.routes[0];
                for (var i = 0; i < route.legs.length; i++) {
                    bounds.extend(route.legs[i].start_location);
                    bounds.extend(route.legs[i].end_location);
                }

                // Adjust the map's viewport to fit the entire route
                map.fitBounds(bounds);
            }
        });
    } catch (error) {
        console.error(error);
    }
}

// Add an event listener to the form element to listen for the submit event and call the handleSubmit function
window.onload = function() {
    initMap();
    const form = document.getElementById("myForm");
    form.addEventListener("submit", handleSubmit);
}
