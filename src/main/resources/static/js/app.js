const API = '';

async function apiGet(url) {
    const res = await fetch(API + url);
    return res.json();
}

async function apiPost(url, data) {
    const res = await fetch(API + url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
    return res.json();
}

async function apiPut(url, data) {
    const res = await fetch(API + url, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data || {})
    });
    return res.json();
}

async function apiDelete(url) {
    const res = await fetch(API + url, { method: 'DELETE' });
    return res.json();
}

function showModal(id) {
    document.getElementById(id).classList.add('show');
}

function hideModal(id) {
    document.getElementById(id).classList.remove('show');
}

function statusBadge(status) {
    const map = {
        'PENDING': '<span class="badge badge-warning">待处理</span>',
        'ARRIVED': '<span class="badge badge-info">已到货</span>',
        'APPROVED': '<span class="badge badge-info">已审核</span>',
        'COMPLETED': '<span class="badge badge-success">已完成</span>',
        'REJECTED': '<span class="badge badge-danger">已拒绝</span>',
        'CANCELLED': '<span class="badge badge-danger">已取消</span>',
        'PICKING': '<span class="badge badge-info">拣货中</span>',
        'SHIPPED': '<span class="badge badge-success">已发货</span>',
        'PRODUCING': '<span class="badge badge-info">生产中</span>',
        'QC_PENDING': '<span class="badge badge-warning">待质检</span>',
        'QC_PASS': '<span class="badge badge-success">质检通过</span>',
        'QC_FAIL': '<span class="badge badge-danger">质检不合格</span>',
        'STORED': '<span class="badge badge-success">已入库</span>',
        'PASS': '<span class="badge badge-success">合格</span>',
        'FAIL': '<span class="badge badge-danger">不合格</span>',
        'REWORK': '<span class="badge badge-warning">返工</span>'
    };
    return map[status] || status;
}

function alert(msg, type) {
    if (type === 'error') {
        alert(msg);
    } else {
        alert(msg);
    }
}

function toast(msg, isError) {
    alert(msg);
}

function formatDate(d) {
    if (!d) return '-';
    return d.substring(0, 10);
}

function formatDateTime(d) {
    if (!d) return '-';
    return d.replace('T', ' ').substring(0, 19);
}
