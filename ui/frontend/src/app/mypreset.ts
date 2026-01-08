import { definePreset } from '@primeuix/themes';
import Aura from '@primeuix/themes/aura';

export const MyPreset = definePreset(Aura, {
    //Your customizations, see the following sections for examples
    semantic: {
        primary: {
            50:  '{indigo.50}',
            100: '{indigo.100}',
            200: '{indigo.200}',
            300: '{indigo.300}',
            400: '{indigo.400}',
            500: '{indigo.500}',
            600: '{indigo.600}',
            700: '{indigo.700}',
            800: '{indigo.800}',
            900: '{indigo.900}',
            950: '{indigo.950}'
        },
        colorScheme: {
            light: {
                surface: {
                    0: '#ffffff',
                    50:  '{cyan.50}',
                    100: '{cyan.100}',
                    200: '{cyan.200}',
                    300: '{cyan.300}',
                    400: '{cyan.400}',
                    500: '{cyan.500}',
                    600: '{cyan.600}',
                    700: '{cyan.700}',
                    800: '{cyan.800}',
                    900: '{cyan.900}',
                    950: '{cyan.950}'
                }

            },
            dark: {
                surface: {
                    0: '#ffffff',
                    100: '{slate.100}',
                    50:  '{slate.50}',
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
        // button: {
        //     // General button properties (applied to all buttons)
        //     root: {
        //         borderRadius: '100px',
        //         paddingX: '1.25rem',
        //         paddingY: '0.625rem',
        //         transitionDuration: '0.2s',
        //         gap: '0.5rem',
        //         primary: {
        //             background: '{blue.600}',
        //             hoverBackground: '{primary.700}',
        //             activeBackground: '{primary.800}',
        //             borderColor: '{primary.600}',
        //             hoverBorderColor: '{primary.700}',
        //             activeBorderColor: '{primary.800}',
        //             color: '#ffffff',
        //             hoverColor: '{blue.900}'
        //         },
        //         secondary: {
        //             background: '{surface.200}',
        //             hoverBackground: '{surface.300}',
        //             color: '{surface.900}'
        //         },
        //         sm: {
        //             fontSize: '0.875rem',
        //             paddingX: '0.75rem',
        //             paddingY: '0.4rem'
        //         },
        //         lg: {
        //             fontSize: '1.25rem',
        //             paddingX: '1.5rem',
        //             paddingY: '0.8rem'
        //         }
        //     },
        //     // Customizing specific severities

        //     // Outline/Text button specific tokens
        //     outlined: {
        //         primary: {
        //             borderColor: '{primary.600}',
        //             hoverBackground: '{primary.50}'
        //         }

        //     },
        //     // Sizes

        // }
        button:{
            root:{
                secondary:{
                    color:'{emerald.600}'
                }
            }
        },
        menubar: {
            root:{
                // borderRadius: '20px'
            },
            colorScheme: {
                light: {
                    root: {
                        background: '{surface.0}',
                        // borderColor: '{blue.200}',
                        
                        padding: '0.60rem 1.5rem',
                        gap: '0.5rem'
                    },
                    // Item specific tokens
                    item: {
                        focusBackground: '{surface.50}',
                        // activeBackground: '{blue.800}',
                        focusColor: '{primary.700}',
                        color: '{surface.700}',
                        borderRadius: '8px',
                        padding: '0.5rem 0.75rem',
                        gap: '0.5rem'
                    },
                    // Submenu specific tokens
                    submenu: {
                        background: '{surface.0}',
                        borderColor: '{surface.100}',
                        borderRadius: '8px',
                        shadow: '{shadow.2}',
                        icon: {
                            size: '0.875rem'
                        }
                    }
                },
                dark:{
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
            },
            // Submenu specific tokens
            submenu: {
                background: '{surface.0}',
                borderColor: '{surface.100}',
                borderRadius: '8px',
                shadow: '{shadow.2}',
                icon: {
                    size: '0.875rem'
                }
            },
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
        }
    }
});
