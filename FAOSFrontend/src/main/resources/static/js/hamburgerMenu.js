// Update hamburgerMenu.js
document.addEventListener('DOMContentLoaded', () => {
  // Handle sidebar hamburger
  const sidebarHamburger = document.querySelector('.sidebar-hamburger');
  const mainHamburger = document.getElementById('hamburger-menu');
  const sidebar = document.querySelector('.sidebar');
  const navList = document.getElementById('nav-list');
  
  if (sidebarHamburger && sidebar) {
      sidebarHamburger.addEventListener('click', (e) => {
          e.stopPropagation();
          sidebar.classList.toggle('active');
      });
  }

  // Handle main header hamburger
  if (mainHamburger && navList) {
      mainHamburger.addEventListener('click', (e) => {
          e.stopPropagation();
          navList.classList.toggle('active');
      });
  }

  // Close menus when clicking outside
  document.addEventListener('click', (e) => {
      if (sidebar && !sidebar.contains(e.target) && !sidebarHamburger?.contains(e.target)) {
          sidebar.classList.remove('active');
      }
      if (navList && !navList.contains(e.target) && !mainHamburger.contains(e.target)) {
          navList.classList.remove('active');
      }
  });
});