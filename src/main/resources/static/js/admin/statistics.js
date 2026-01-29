document.addEventListener("DOMContentLoaded", function() {
	
	// 1. 데이터 확인 (브라우저 콘솔에서 F12로 확인 가능)
	    const data = window.chartData;
	    if (!data) return;
	
    // 공통 옵션 설정
    const commonOptions = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { display: false }
        },
        scales: {
            y: { beginAtZero: true, ticks: { size: 10 } },
            x: { ticks: { size: 10 } }
        }
    };

	// 1. 회원 가입 추이 (Line)
	    const userCtx = document.getElementById('userTrendChart');
	    if (userCtx) {
	        new Chart(userCtx, {
	            type: 'line',
	            data: {
	                labels: data.labels,
	                datasets: [{
	                    label: '신규 가입',
	                    data: data.userCounts,
	                    borderColor: '#13ec5b',
	                    backgroundColor: 'rgba(19, 236, 91, 0.1)',
	                    fill: true,
	                    tension: 0.4
	                }]
	            },
	            options: commonOptions
	        });
	    }

		// 2. 상품 등록 추이 (Bar)
		    const prodCtx = document.getElementById('productTrendChart');
		    if (prodCtx) {
		        new Chart(prodCtx, {
		            type: 'bar',
		            data: {
		                labels: data.labels,
		                datasets: [{
		                    label: '상품 등록',
		                    data: data.productCounts,
		                    backgroundColor: '#3b82f6',
		                    borderRadius: 5
		                }]
		            },
		            options: commonOptions
		        });
		    }

    // 3. 거래 완료 추이 (Area/Line) - 새로 추가
	    const orderCtx = document.getElementById('orderTrendChart');
	    if (orderCtx) {
	        new Chart(orderCtx, {
	            type: 'line',
	            data: {
	                labels: data.labels,
	                datasets: [{
	                    label: '거래 완료',
	                    data: data.orderCounts,
	                    borderColor: '#f59e0b',
	                    backgroundColor: 'rgba(245, 158, 11, 0.1)',
	                    fill: true,
	                    tension: 0.4
	                }]
	            },
	            options: commonOptions
	        });
	    }

    // 4. 카테고리별 상품 분포 (Doughnut)
    new Chart(document.getElementById('categoryPieChart'), {
        type: 'doughnut',
        data: {
            labels: ['디지털', '가전', '가구', '의류', '기타'],
            datasets: [{
                data: [40, 25, 15, 10, 10],
                backgroundColor: ['#13ec5b', '#3b82f6', '#f59e0b', '#a855f7', '#ef4444'],
                borderWidth: 0
            }]
        },
        options: {
            ...commonOptions,
            plugins: { legend: { display: true, position: 'bottom' } }
        }
    });
});