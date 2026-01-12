import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeuix/themes/Aura';
import { definePreset } from '@primeuix/themes';



 
 
// const MyBluePreset = definePreset(Aura, {
//     semantic: {
//         // 1. PRIMARY: The "Sea Blue" Identity
//         // We use 'cyan' here for that bright, tropical water feel. 
//         // Alternatively, use 'sky' for a deeper ocean look or 'teal' for a greener lagoon look.
//         primary: {
//             50: '{cyan.50}', 100: '{cyan.100}', 200: '{cyan.200}', 300: '{cyan.300}',
//             400: '{cyan.400}', 500: '{cyan.500}', 600: '{cyan.600}', 700: '{cyan.700}',
//             800: '{cyan.800}', 900: '{cyan.900}', 950: '{cyan.950}'
//         },

//         // 2. SURFACE: The "Sand & Stone" 
//         // Kept 'slate' as it provides a cool, crisp background that contrasts well with blue.
//         surface: {
//             0: '#ffffff', 50: '{slate.50}', 100: '{slate.100}', 200: '{slate.200}',
//             300: '{slate.300}', 400: '{slate.400}', 500: '{slate.500}', 600: '{slate.600}',
//             700: '{slate.700}', 800: '{slate.800}', 900: '{slate.900}', 950: '{slate.950}'
//         },

//         colorScheme: {
//             light: {
//                 // Primary Action Styles (Book Now buttons, etc.)
//                 primary: {
//                     color: '{primary.500}', // Slightly brighter (500) than purple for a vibrant travel feel
//                     contrastColor: '#ffffff',
//                     hoverColor: '{primary.600}',
//                     activeColor: '{primary.700}'
//                 },
//                 // ACCENT/HIGHLIGHT (Date pickers, active list items)
//                 highlight: {
//                     background: '{primary.50}',  // Very light wash (like foam)
//                     focusBackground: '{primary.100}',
//                     color: '{primary.700}',      // Deep ocean text
//                     focusColor: '{primary.800}'
//                 },
//                 // SECONDARY
//                 secondary: {
//                     background: '{surface.100}', // Lighter than purple preset for an "airy" feel
//                     color: '{surface.700}',
//                     hoverBackground: '{surface.200}'
//                 }
//             },
//             dark: {
//                 primary: {
//                     color: '{primary.400}',
//                     contrastColor: '{surface.950}',
//                     hoverColor: '{primary.300}',
//                     activeColor: '{primary.200}'
//                 },
//                 highlight: {
//                     // UPDATED RGB: This corresponds to Cyan-500 (6, 182, 212)
//                     // The previous code had hardcoded Purple RGB values here.
//                     background: 'rgba(6, 182, 212, 0.16)',
//                     focusBackground: 'rgba(6, 182, 212, 0.24)',
//                     color: 'rgba(255,255,255,.87)',
//                     focusColor: 'rgba(255,255,255,.87)'
//                 }
//             }
//         },

//         // 3. COMMON COMPONENT SEMANTICS
//         formField: {
//             paddingX: '1rem', // Slightly wider padding for ease of use
//             paddingY: '0.75rem',
//             borderRadius: '12px', // Increased radius: Rounder = Friendlier/Relaxed vibe
//             focusRing: {
//                 width: '3px', // Thicker focus ring for clear accessibility
//                 style: 'solid',
//                 color: '{primary.400}', // Lighter ring color
//                 offset: '2px'
//             }
//         },
        
//         overlay: {
//             borderRadius: '16px', // Very rounded cards/modals
//             shadow: '0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1)' // Softer, deeper shadow (floating effect)
//         },

//         navigation: {
//             item: {
//                 borderRadius: '12px'
//             }
//         }
//     }
// });
export const MyGradientPreset = definePreset(Aura, {
    semantic: {
        primary: {
            50: '{blue.50}', 100: '{blue.100}', 200: '{blue.200}', 300: '{blue.300}',
            400: '{blue.400}', 500: 'rgb(33, 33, 214)', 600: 'rgb(80, 35, 217)',
            700: 'rgb(132, 37, 221)', 800: '{indigo.800}', 900: '{indigo.900}', 950: '{indigo.950}'
        }
    },
    components: {
        button: {
            colorScheme: {
                light: {
                    // In Aura, the 'primary' button tokens live inside 'root' 
                    // within the colorScheme
                    root: {
                        primary: {
                            background: 'linear-gradient(135deg, rgb(33, 33, 214) 50%, rgb(132, 37, 221) 100%)',
                            hoverBackground: 'linear-gradient(135deg, rgb(43, 43, 224) 50%, rgb(142, 47, 231) 100%)',
                            activeBackground: 'linear-gradient(135deg, rgb(23, 23, 204) 50%, rgb(122, 27, 211) 100%)',
                            color: '#ffffff',
                            borderColor: 'transparent'
                        }
                    }
                },
                dark: {
                    root: {
                        primary: {
                            background: 'linear-gradient(135deg, rgb(33, 33, 214) 50%, rgb(132, 37, 221) 100%)',
                            hoverBackground: 'linear-gradient(135deg, rgb(43, 43, 224) 50%, rgb(142, 47, 231) 100%)',
                            activeBackground: 'linear-gradient(135deg, rgb(23, 23, 204) 50%, rgb(122, 27, 211) 100%)',
                            color: '#ffffff',
                            borderColor: 'transparent'
                        }
                    }
                }
            }
        },
        tag: {
    // 1. Layout properties go in the top-level root
    root: {
                borderRadius: '12px',
                padding: '0.2rem 0.6rem'
            },
            colorScheme: {
                light: {
                    // 2. Color properties MUST be inside a severity key
                    // The 'primary' key is where 'background' is a known property for Tag
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
export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes), provideClientHydration(withEventReplay()),
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
