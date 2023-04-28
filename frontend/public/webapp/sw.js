/* eslint-disable */
importScripts('https://storage.googleapis.com/workbox-cdn/releases/6.1.5/workbox-sw.js')

const CACHE = 'guessaiword'

const offlineFallbackPage = '/offline.html'

self.addEventListener('message', (event) => {
  if (event.data && event.data.type === 'SKIP_WAITING') {
    self.skipWaiting()
  }
})

self.addEventListener('install', async (event) => {
  event.waitUntil(
    caches.open(CACHE)
      .then((cache) => cache.add(offlineFallbackPage))
  )
})

if (workbox.navigationPreload.isSupported()) {
  workbox.navigationPreload.enable()
}

self.addEventListener('fetch', (event) => {
  if (event.request.mode === 'navigate') {
    event.respondWith((async () => {
      try {
        const preloadResp = await event.preloadResponse

        if (preloadResp) {
          return preloadResp
        }

        const networkResp = await fetch(event.request)
        return networkResp
      } catch (error) {
        const cache = await caches.open(CACHE)
        const cachedResp = await cache.match(offlineFallbackPage)
        return cachedResp
      }
    })())
  }
})


workbox.routing.setDefaultHandler(
  new workbox.strategies.NetworkOnly()
);
