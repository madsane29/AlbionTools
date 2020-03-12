$(document).ready(
		function() {

			// SUBMIT FORM
			$("#inputForm").submit(function(event) {
				// Prevent the form from submitting via the browser.
				event.preventDefault();
				ajaxPost();
			});

			function ajaxPost() {

				var formData = {
					fromCity : $('#fromCity').val(),
					toCity : $('#toCity').val(),
					profitMinimum : $('#profitMinimum').val(),
					profitMaximum : $('#profitMaximum').val(),
					auctionTax : $("input[name='auctionTax']:checked").val()
				}
				/* DO POST */
				/*
				 * console.log(result[i].itemName + ' ' + result[i].fromCity + ' ' +
				 * result[i].fromPrice + ' ' + result[i].toCity + ' ' +
				 * result[i].toPrice + ' ' + result[i].profit);
				 */
				$('html, body').css("cursor", "wait");
				$.ajax({
					type : "POST",
					contentType : "application/json",
					url : window.location + "/data",
					data : JSON.stringify(formData),
					dataType : 'json',
					success : function(result) {
						$("tbody").children().remove();
						var tableRef = document.getElementById('table')
								.getElementsByTagName('tbody')[0];
						for (var i = 0; i < result.length; i++) {

							var newRow = tableRef.insertRow(table.length);
							newRow.className = "row100";
							for (var j = 0; j < 6; j++) {
								var cell = newRow.insertCell(j);
								switch (j) {
								case 0:
									cell.innerHTML = result[i].itemName;
									break;
								case 1:
									cell.innerHTML = result[i].fromCity;
									break;
								case 2:
									cell.innerHTML = result[i].fromPrice;
									break;
								case 3:
									cell.innerHTML = result[i].toCity;
									break;
								case 4:
									cell.innerHTML = result[i].toPrice;
									break;
								case 5:
									cell.innerHTML = result[i].profit;
									break;
								}
							}
						}
						$('html, body').css("cursor", "default");
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
						$('html, body').css("cursor", "default");
					}
				});
				return false;
			}
		})