import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

// PrimeNG Imports
import { providePrimeNG } from 'primeng/config';
import { MessageService } from 'primeng/api';
import Aura from '@primeuix/themes/Aura';
import { definePreset } from '@primeuix/themes';

import { routes } from './app.routes';

// 1. Define your custom theme preset
export const MyGradientPreset = definePreset(Aura, {
    semantic: {
        primary: {
            50: '{black.950}', 100: '{blue.100}', 200: '{blue.200}', 300: '{blue.300}',
            400: '{blue.400}', 500: 'rgb(33, 33, 214)', 600: 'rgb(80, 35, 217)',
            700: 'rgb(132, 37, 221)', 800: '{indigo.800}', 900: '{indigo.900}', 950: '{indigo.950}'
        }
    },
    components: {
       
        tag: {
            root: {
                borderRadius: '12px',
                padding: '0.2rem 0.6rem'
            },
            colorScheme: {
                light: {
                    primary: {
                        background: 'linear-gradient(135deg, rgb(33, 33, 214) 0%, rgb(132, 37, 221) 100%)',
                        color: '#ffffff'
                    }
                },
                dark: {
                    primary: {
                        background: 'linear-gradient(135deg, rgba(16, 16, 101, 1) 0%, rgba(54, 14, 92, 1) 100%)',
                        color: '#ffffff'
                    }
                }
            }
        }
    }
});

// 2. Export the SINGLE merged appConfig
export const appConfig: ApplicationConfig = {
    providers: [
        provideBrowserGlobalErrorListeners(),
        provideRouter(routes),
        
        provideAnimationsAsync(), // Required for PrimeNG animations (Toast, etc.)
        MessageService,           // Required for Toast messages
        providePrimeNG({
            theme: {
                preset: MyGradientPreset,
                options: {
                    prefix: 'p',
                    darkModeSelector: '.dark',
                    cssLayer: false
                }
            }
        })
    ]
};