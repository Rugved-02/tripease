import { definePreset } from '@primeuix/themes';
import Aura from '@primeuix/themes/aura';

export const MyPreset = definePreset(Aura, {
    //Your customizations, see the following sections for examples
    semantic: {
        primary: {
            0: '#ffffff',
            50: '{blue.50}',
            100: '{blue.100}',
            200: '{blue.200}',
            300: '{blue.300}',
            400: '{blue.400}',
            500: '{blue.500}',
            600: '{blue.600}',
            700: '{blue.700}',
            800: '{blue.800}',
            900: '{blue.900}',
            950: '{blue.950}',
            1000: '#000000ff',
            1050: '#0000001a',
            1100: '#585858ff'
        },
        colorScheme: {
            light: {
                surface: {
                    0: '#ffffff',
                    50: '{cyan.50}',
                    100: '{cyan.100}',
                    200: '{cyan.200}',
                    300: '{cyan.300}',
                    400: '{cyan.400}',
                    500: '{cyan.500}',
                    600: '{cyan.600}',
                    700: '{cyan.700}',
                    800: '{cyan.800}',
                    900: '{cyan.900}',
                    950: '{cyan.950}',
                }

            },
            dark: {
                surface: {
                    0: '#ffffff',
                    100: '{slate.100}',
                    50: '{slate.50}',
                    200: '{slate.200}',
                    300: '{slate.300}',
                    400: '{slate.400}',
                    500: '{slate.500}',
                    600: '{slate.600}',
                    700: '{slate.700}',
                    800: '{slate.800}',
                    900: '{slate.900}',
                    950: '{slate.950}'
                }
            }
        }
    },
    components: {
        
        menubar: {
            root: {
                // borderRadius: '20px',
                borderColor: '{surface.0}'
            },
            colorScheme: {
                light: {
                    root: {
                        background: '{surface.0}',
                        // borderColor: '{blue.200}',

                        padding: '0.80rem 1.5rem',
                        gap: '0.5rem'
                    },
                    // Item specific tokens
                    item: {
                        focusBackground: '{surface.50}',
                        // activeBackground: '{blue.800}',
                        focusColor: '{primary.600}',
                        color: '{primary.1100}',
                        borderRadius: '8px',
                        padding: '0.1rem 0.75rem',
                        gap: '0.5rem'
                    }
                },
                dark: {
                    root: {
                        background: '{surface.900}',
                        // borderColor: '{blue.200}',
                        // borderRadius: '12px',
                        padding: '0.60rem 1.5rem',
                        gap: '0.5rem'
                    },
                    // Item specific tokens
                    item: {
                        focusBackground: '{surface.800}',
                        // activeBackground: '{blue.800}',
                        focusColor: '{primary.700}',
                        color: '{surface.100}',
                        borderRadius: '8px',
                        padding: '0.5rem 0.75rem',
                        gap: '0.5rem'
                    }
                }
            },
            // The main container tokens are nested under 'root'

            separator: {
                borderColor: '{surface.200}'
            },
            
            // mobileButton: {
            //     borderRadius: '50%',
            //     // width: '2.5rem',
            //     // height: '2.5rem'
            // }
        },
        tag: {
            root: {
                borderRadius: '12px',
                padding: '0.2rem 0.6rem'
            },
            colorScheme: {
                light: {
                    // PREVIOUS SETTING: Tag keeps the gradient
                    primary: {
                        background: 'linear-gradient(135deg, rgb(33, 33, 214) 0%, rgb(132, 37, 221) 100%)',
                        color: '#ffffff'
                    }
                },
                dark: {
                    // PREVIOUS SETTING: Tag keeps the dark gradient
                    primary: {
                        background: 'linear-gradient(135deg, rgba(16, 16, 101, 1) 0%, rgba(54, 14, 92, 1) 100%)',
                        color: '#ffffff'
                    }
                }
            }
        }
 
    }
});
