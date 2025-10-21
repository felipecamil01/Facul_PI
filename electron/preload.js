const { contextBridge } = require('electron');

// Expose safe APIs here if the renderer needs to communicate with main
contextBridge.exposeInMainWorld('electronAPI', {
  // placeholder for future APIs
});

