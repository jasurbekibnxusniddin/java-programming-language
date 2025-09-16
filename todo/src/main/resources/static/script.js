// Global variables
let currentUser = null;
let currentFilter = 'all';
let todos = [];

// API Base URL
const API_BASE = '/v1';

// DOM Elements
const authSection = document.getElementById('auth-section');
const appSection = document.getElementById('app-section');
const userInfo = document.getElementById('user-info');
const usernameDisplay = document.getElementById('username-display');
const loginTab = document.getElementById('login-tab');
const registerTab = document.getElementById('register-tab');
const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');
const todoList = document.getElementById('todo-list');
const loading = document.getElementById('loading');
const errorMessage = document.getElementById('error-message');
const successMessage = document.getElementById('success-message');

// Initialize app
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    attachEventListeners();
});

// Initialize application
function initializeApp() {
    // Check if user is logged in (simple localStorage check)
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
        currentUser = JSON.parse(savedUser);
        showApp();
        loadUserTodos();
        loadUserStats();
    } else {
        showAuth();
    }
}

// Attach event listeners
function attachEventListeners() {
    // Auth tabs
    loginTab.addEventListener('click', () => switchTab('login'));
    registerTab.addEventListener('click', () => switchTab('register'));
    
    // Forms
    document.getElementById('loginForm').addEventListener('submit', handleLogin);
    document.getElementById('registerForm').addEventListener('submit', handleRegister);
    document.getElementById('todoForm').addEventListener('submit', handleAddTodo);
    document.getElementById('edit-todo-form').addEventListener('submit', handleEditTodo);
    
    // Logout
    document.getElementById('logout-btn').addEventListener('click', handleLogout);
    
    // Filter buttons
    document.getElementById('filter-all').addEventListener('click', () => setFilter('all'));
    document.getElementById('filter-pending').addEventListener('click', () => setFilter('pending'));
    document.getElementById('filter-completed').addEventListener('click', () => setFilter('completed'));
    
    // Search
    document.getElementById('search-btn').addEventListener('click', handleSearch);
    document.getElementById('search-input').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') handleSearch();
    });
    
    // Modal
    document.querySelector('.close').addEventListener('click', closeModal);
    document.getElementById('cancel-edit').addEventListener('click', closeModal);
}

// Switch authentication tabs
function switchTab(tab) {
    if (tab === 'login') {
        loginTab.classList.add('active');
        registerTab.classList.remove('active');
        loginForm.style.display = 'block';
        registerForm.style.display = 'none';
    } else {
        registerTab.classList.add('active');
        loginTab.classList.remove('active');
        registerForm.style.display = 'block';
        loginForm.style.display = 'none';
    }
}

// Show authentication section
function showAuth() {
    authSection.style.display = 'block';
    appSection.style.display = 'none';
    userInfo.style.display = 'none';
}

// Show main application
function showApp() {
    authSection.style.display = 'none';
    appSection.style.display = 'grid';
    userInfo.style.display = 'flex';
    usernameDisplay.textContent = currentUser.username;
}

// Handle user registration
async function handleRegister(e) {
    e.preventDefault();
    
    const username = document.getElementById('register-username').value;
    const password = document.getElementById('register-password').value;
    
    if (!username || !password) {
        showError('Please fill in all fields');
        return;
    }
    
    try {
        showLoading(true);
        
        const response = await fetch(`${API_BASE}/user/create`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password })
        });
        
        if (response.ok) {
            const user = await response.json();
            currentUser = user;
            localStorage.setItem('currentUser', JSON.stringify(user));
            showSuccess('Registration successful!');
            showApp();
            loadUserTodos();
            loadUserStats();
        } else {
            const errorText = await response.text();
            showError(`Registration failed: ${errorText}`);
        }
    } catch (error) {
        showError('Registration failed. Please try again.');
        console.error('Registration error:', error);
    } finally {
        showLoading(false);
    }
}

// Handle user login - simplified for demo (no authentication endpoint)
async function handleLogin(e) {
    e.preventDefault();
    
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;
    
    if (!username || !password) {
        showError('Please fill in all fields');
        return;
    }
    
    try {
        showLoading(true);
        
        // For demo purposes, we'll get user by username
        const response = await fetch(`${API_BASE}/user/username/${username}`);
        
        if (response.ok) {
            const user = await response.json();
            currentUser = user;
            localStorage.setItem('currentUser', JSON.stringify(user));
            showSuccess('Login successful!');
            showApp();
            loadUserTodos();
            loadUserStats();
        } else {
            showError('User not found. Please register first.');
        }
    } catch (error) {
        showError('Login failed. Please try again.');
        console.error('Login error:', error);
    } finally {
        showLoading(false);
    }
}

// Handle logout
function handleLogout() {
    currentUser = null;
    todos = [];
    localStorage.removeItem('currentUser');
    showAuth();
    document.getElementById('loginForm').reset();
    document.getElementById('registerForm').reset();
    clearMessages();
}

// Handle add todo
async function handleAddTodo(e) {
    e.preventDefault();
    
    const title = document.getElementById('todo-title').value;
    const completed = document.getElementById('todo-completed').checked;
    
    if (!title.trim()) {
        showError('Please enter a todo title');
        return;
    }
    
    try {
        showLoading(true);
        
        const response = await fetch(`${API_BASE}/todo/create`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                title: title.trim(),
                completed: completed,
                userId: currentUser.id
            })
        });
        
        if (response.ok) {
            const newTodo = await response.json();
            showSuccess('Todo added successfully!');
            document.getElementById('todoForm').reset();
            loadUserTodos();
            loadUserStats();
        } else {
            const errorText = await response.text();
            showError(`Failed to add todo: ${errorText}`);
        }
    } catch (error) {
        showError('Failed to add todo. Please try again.');
        console.error('Add todo error:', error);
    } finally {
        showLoading(false);
    }
}

// Load user todos
async function loadUserTodos() {
    if (!currentUser) return;
    
    try {
        showLoading(true);
        
        const response = await fetch(`${API_BASE}/todo/user/${currentUser.id}`);
        
        if (response.ok) {
            todos = await response.json();
            renderTodos();
        } else {
            showError('Failed to load todos');
        }
    } catch (error) {
        showError('Failed to load todos');
        console.error('Load todos error:', error);
    } finally {
        showLoading(false);
    }
}

// Render todos based on current filter
function renderTodos() {
    let filteredTodos = todos;
    
    // Apply filter
    if (currentFilter === 'pending') {
        filteredTodos = todos.filter(todo => !todo.completed);
    } else if (currentFilter === 'completed') {
        filteredTodos = todos.filter(todo => todo.completed);
    }
    
    // Update filter buttons
    document.querySelectorAll('.btn-filter').forEach(btn => btn.classList.remove('active'));
    document.getElementById(`filter-${currentFilter}`).classList.add('active');
    
    // Render todos
    todoList.innerHTML = '';
    
    if (filteredTodos.length === 0) {
        todoList.innerHTML = '<p style="text-align: center; color: #666; padding: 20px;">No todos found</p>';
        return;
    }
    
    filteredTodos.forEach(todo => {
        const todoItem = createTodoElement(todo);
        todoList.appendChild(todoItem);
    });
}

// Create todo element
function createTodoElement(todo) {
    const todoItem = document.createElement('div');
    todoItem.className = `todo-item ${todo.completed ? 'completed' : ''}`;
    
    todoItem.innerHTML = `
        <div class="todo-content">
            <input type="checkbox" class="todo-checkbox" ${todo.completed ? 'checked' : ''} 
                   onchange="toggleTodoStatus(${todo.id}, this.checked)">
            <span class="todo-title ${todo.completed ? 'completed' : ''}">${escapeHtml(todo.title)}</span>
        </div>
        <div class="todo-actions">
            <button class="btn btn-edit" onclick="editTodo(${todo.id})">Edit</button>
            <button class="btn btn-danger" onclick="deleteTodo(${todo.id})">Delete</button>
        </div>
    `;
    
    return todoItem;
}

// Toggle todo status
async function toggleTodoStatus(todoId, completed) {
    try {
        const response = await fetch(`${API_BASE}/todo/${todoId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ completed })
        });
        
        if (response.ok) {
            const updatedTodo = await response.json();
            // Update local todos array
            const todoIndex = todos.findIndex(t => t.id === todoId);
            if (todoIndex !== -1) {
                todos[todoIndex] = updatedTodo;
            }
            renderTodos();
            loadUserStats();
            showSuccess(`Todo marked as ${completed ? 'completed' : 'pending'}!`);
        } else {
            showError('Failed to update todo status');
            // Revert checkbox state
            loadUserTodos();
        }
    } catch (error) {
        showError('Failed to update todo status');
        console.error('Toggle todo error:', error);
        loadUserTodos();
    }
}

// Edit todo
function editTodo(todoId) {
    const todo = todos.find(t => t.id === todoId);
    if (!todo) return;
    
    document.getElementById('edit-todo-id').value = todo.id;
    document.getElementById('edit-todo-title').value = todo.title;
    document.getElementById('edit-todo-completed').checked = todo.completed;
    
    document.getElementById('edit-modal').style.display = 'flex';
}

// Handle edit todo form submission
async function handleEditTodo(e) {
    e.preventDefault();
    
    const todoId = document.getElementById('edit-todo-id').value;
    const title = document.getElementById('edit-todo-title').value;
    const completed = document.getElementById('edit-todo-completed').checked;
    
    if (!title.trim()) {
        showError('Please enter a todo title');
        return;
    }
    
    try {
        showLoading(true);
        
        const response = await fetch(`${API_BASE}/todo/${todoId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                title: title.trim(),
                completed: completed
            })
        });
        
        if (response.ok) {
            const updatedTodo = await response.json();
            showSuccess('Todo updated successfully!');
            closeModal();
            loadUserTodos();
            loadUserStats();
        } else {
            const errorText = await response.text();
            showError(`Failed to update todo: ${errorText}`);
        }
    } catch (error) {
        showError('Failed to update todo. Please try again.');
        console.error('Edit todo error:', error);
    } finally {
        showLoading(false);
    }
}

// Delete todo
async function deleteTodo(todoId) {
    if (!confirm('Are you sure you want to delete this todo?')) return;
    
    try {
        showLoading(true);
        
        const response = await fetch(`${API_BASE}/todo/${todoId}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showSuccess('Todo deleted successfully!');
            loadUserTodos();
            loadUserStats();
        } else {
            showError('Failed to delete todo');
        }
    } catch (error) {
        showError('Failed to delete todo');
        console.error('Delete todo error:', error);
    } finally {
        showLoading(false);
    }
}

// Handle search
async function handleSearch() {
    const keyword = document.getElementById('search-input').value.trim();
    
    if (!keyword) {
        loadUserTodos();
        return;
    }
    
    try {
        showLoading(true);
        
        // Use the search endpoint with current filter
        const completed = currentFilter === 'completed' ? true : 
                         currentFilter === 'pending' ? false : 
                         true; // Default for 'all' - we'll get both
        
        const response = await fetch(`${API_BASE}/todo/search?keyword=${encodeURIComponent(keyword)}&completed=${completed}`);
        
        if (response.ok) {
            let searchResults = await response.json();
            
            // If filter is 'all', we need to search both completed and pending
            if (currentFilter === 'all') {
                const pendingResponse = await fetch(`${API_BASE}/todo/search?keyword=${encodeURIComponent(keyword)}&completed=false`);
                if (pendingResponse.ok) {
                    const pendingResults = await pendingResponse.json();
                    searchResults = [...searchResults, ...pendingResults];
                    // Remove duplicates
                    searchResults = searchResults.filter((todo, index, self) => 
                        index === self.findIndex(t => t.id === todo.id)
                    );
                }
            }
            
            todos = searchResults;
            renderTodos();
        } else {
            showError('Search failed');
        }
    } catch (error) {
        showError('Search failed');
        console.error('Search error:', error);
    } finally {
        showLoading(false);
    }
}

// Set filter
function setFilter(filter) {
    currentFilter = filter;
    renderTodos();
    
    // Clear search when changing filter
    document.getElementById('search-input').value = '';
    
    // Reload todos to get fresh data
    loadUserTodos();
}

// Load user statistics
async function loadUserStats() {
    if (!currentUser) return;
    
    try {
        const response = await fetch(`${API_BASE}/todo/statistics/user/${currentUser.id}`);
        
        if (response.ok) {
            const stats = await response.json();
            renderStats(stats);
        }
    } catch (error) {
        console.error('Failed to load stats:', error);
    }
}

// Render user statistics
function renderStats(stats) {
    const statsContainer = document.getElementById('user-stats');
    
    if (!stats || stats.length === 0) {
        statsContainer.innerHTML = '<p>No statistics available</p>';
        return;
    }
    
    // Stats array format: [username, total_todos, completed_todos, pending_todos]
    const [username, totalTodos, completedTodos, pendingTodos] = stats;
    
    statsContainer.innerHTML = `
        <div class="stat-card">
            <div class="stat-number">${totalTodos || 0}</div>
            <div class="stat-label">Total Todos</div>
        </div>
        <div class="stat-card">
            <div class="stat-number">${completedTodos || 0}</div>
            <div class="stat-label">Completed</div>
        </div>
        <div class="stat-card">
            <div class="stat-number">${pendingTodos || 0}</div>
            <div class="stat-label">Pending</div>
        </div>
        <div class="stat-card">
            <div class="stat-number">${totalTodos > 0 ? Math.round((completedTodos / totalTodos) * 100) : 0}%</div>
            <div class="stat-label">Completion Rate</div>
        </div>
    `;
}

// Close modal
function closeModal() {
    document.getElementById('edit-modal').style.display = 'none';
    document.getElementById('edit-todo-form').reset();
}

// Utility functions
function showLoading(show) {
    loading.style.display = show ? 'flex' : 'none';
}

function showError(message) {
    errorMessage.textContent = message;
    errorMessage.style.display = 'block';
    setTimeout(() => {
        errorMessage.style.display = 'none';
    }, 5000);
}

function showSuccess(message) {
    successMessage.textContent = message;
    successMessage.style.display = 'block';
    setTimeout(() => {
        successMessage.style.display = 'none';
    }, 3000);
}

function clearMessages() {
    errorMessage.style.display = 'none';
    successMessage.style.display = 'none';
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Handle clicks outside modal to close it
window.onclick = function(event) {
    const modal = document.getElementById('edit-modal');
    if (event.target === modal) {
        closeModal();
    }
}