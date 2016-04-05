$(document).foundation();

$(document).ready(function () {

    $("#searchbutton").click(function () {
        callEndpoint1().then(function (dataToRender) {
            var nodeToInsertTo = document.getElementById("endpoint1resultHolder");
            nodeToInsertTo.innerHTML = '';
            
            dataToRender.forEach(function (result) {
                var resultHolder = document.createElement("div");
                resultHolder.classList.add("result");
                var innerHtml = "<span title='Name'>" + result.name + "</span><br/>";
                resultHolder.innerHTML = innerHtml;
                nodeToInsertTo.appendChild(resultHolder);
            });
        });

        callEndpoint2().then(function (dataToRender) {
            var nodeToInsertTo = document.getElementById("endpoint2resultHolder");
            nodeToInsertTo.innerHTML = '';
            
            dataToRender.forEach(function (result) {
                var resultHolder = document.createElement("div");
                resultHolder.classList.add("result");
                var innerHtml = "<span title='Name'>" + result.name + "</span><br/>";
                resultHolder.innerHTML = innerHtml;
                nodeToInsertTo.appendChild(resultHolder);
            });
        });

    });

    function callEndpoint1() {
        return new Promise(function (resolve) {
            $.ajax({
                method: "GET",
                url: "/jpa-cases"
            })
            .done(function (msg) {
                resolve(msg._embedded.cases);
            });
        });
    }

    function callEndpoint2() {
        return new Promise(function (resolve) {
            $.ajax({
                  method: "GET",
                  url: "/elastic-cases"
              })
              .done(function (msg) {
                  resolve(msg._embedded.cases);
              });
        });
    }

});
