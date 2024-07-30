// Getting the context of the canvas element we want to select
var ctx = document.getElementById('myLineChart').getContext('2d');

// Creating a new Chart instance
var myLineChart = new Chart(ctx, {
	type: 'line',
	data: {
		labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6',
		 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
		datasets: [{
				label: 'Panda 1',
				backgroundColor: 'rgba(255, 163, 80, 0.1)',
				borderColor: 'rgba(255, 132, 42, 1)',
				data: [65, 59, 80, 81, 56, 55, 46, 70, 50, 42, 60, 85],
				fill: true
			},
			{
				label: 'Panda 2',
				backgroundColor: 'rgba(41, 134, 191, 0.1)',
				borderColor: 'rgba(41, 132, 191, 1)',
				data: [45, 72, 59, 49, 59, 70, 50, 63, 77, 56, 55, 70],
				fill: true
			},
			{
				label: 'Panda 3',
				backgroundColor: 'rgba(41, 233, 191, 0.1)',
				borderColor: 'rgba(41, 233, 191, 1)',
				data: [45, 69, 53, 60, 50, 75, 60, 53, 70, 46, 50, 62],
				fill: true
			}
		]
	},
	options: {
		responsive: true,
		plugins: {
			legend: {
				position: 'top',
			},
			title: {
				display: true
			}
		},
		scales: {
			x: {
				display: true,
				title: {
					display: true,
					text: 'Tháng'
				}
			},
			y: {
				display: true,
				title: {
					display: true,
					text: 'Value'
				}
			}
		}
	}
});