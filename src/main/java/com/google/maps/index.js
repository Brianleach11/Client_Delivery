let map;

async function initMap() {
    // The location of Uluru
    const position = { lat: 26.0797377, lng: -80.27373039 };
    // Request needed libraries.
    //@ts-ignore
    const { Map } = await google.maps.importLibrary("maps");

    // The map, centered at Uluru
    map = new Map(document.getElementById("map"), {
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

    const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");
    const { LatLng } = await google.maps.importLibrary("core");

    // Get the value of the addresses input field
    const addresses = document.getElementById("inputAddresses").value.split(",");
    // Call getOptimizedRoute with the addresses value
    try {
        let jsonData = await getOptimizedRoute(addresses);
        const latLngList = Object.values(jsonData);

        for (let item of latLngList) {
            var newMarker = new AdvancedMarkerElement({
                map: map,
                position: item
            });
        }
    } catch (error) {
        console.error(error);
    }

}

initMap();

// Add an event listener to the form element to listen for the submit event and call the handleSubmit function
const form = document.getElementById("myForm");
form.addEventListener("submit", handleSubmit);