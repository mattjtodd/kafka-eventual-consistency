$(document).foundation();

$(document).ready(function () {
    // do endoint1 get results
    var resultsEndpoint1 = [{
        caseId: "1",
        name: {
            firstName: "Tom",
            lastName: "Chamberlain"
        },
        group: {
            groupId: "G1",
            groupName: "Group1"
        },
        user: {
            userId: "U1",
            userName: "UserOne"
        },
        creationDate: "10-10-2016",
        updateDate: "10-10-2016"
    },
        {
            caseId: "2",
            name: {
                firstName: "Matt",
                lastName: "Todd"
            },
            group: {
                groupId: "G2",
                groupName: "Group2"
            },
            user: {
                userId: "U12",
                userName: "UserTwo"
            },
            creationDate: "10-11-2016",
            updateDate: "10-11-2016"
        }];

    $("#searchbutton").click(function () {
        callEndpoint1($("#searchQuery").val()).then(function (dataToRender) {
            var nodeToInsertTo = document.getElementById("endpoint1resultHolder");
            nodeToInsertTo.innerHTML = '';
            
            dataToRender.forEach(function (result) {
                var resultHolder = document.createElement("div");
                resultHolder.classList.add("result");
                var innerHtml = "<span title='Case Id'>" + result.eventId + "</span><br/>";
                innerHtml += "<span title='Name'>" + result.name + "</span><br/>";
                resultHolder.innerHTML = innerHtml;
                nodeToInsertTo.appendChild(resultHolder);
            });
        });

        callEndpoint2($("#searchQuery").val()).then(function (dataToRender) {
            var nodeToInsertTo = document.getElementById("endpoint2resultHolder");
            nodeToInsertTo.innerHTML = '';
            
            dataToRender.forEach(function (result) {
                var resultHolder = document.createElement("div");
                resultHolder.classList.add("result");
                var innerHtml = "<span title='Case Id'>" + result.eventId + "</span><br/>";
                innerHtml += "<span title='Name'>" + result.name + "</span><br/>";
                resultHolder.innerHTML = innerHtml;
                nodeToInsertTo.appendChild(resultHolder);
            });
        });

    });

    function callEndpoint1(dataToSearchOn) {
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

    function callEndpoint2(dataToSearchOn) {
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
