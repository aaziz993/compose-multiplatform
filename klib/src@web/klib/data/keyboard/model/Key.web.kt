package klib.data.keyboard.model

import klib.data.type.collections.bimap.BiMap
import klib.data.type.collections.bimap.biMapOf
import web.keyboard.AltLeft
import web.keyboard.AltRight
import web.keyboard.ArrowDown
import web.keyboard.ArrowLeft
import web.keyboard.ArrowRight
import web.keyboard.ArrowUp
import web.keyboard.AudioVolumeDown
import web.keyboard.AudioVolumeMute
import web.keyboard.AudioVolumeUp
import web.keyboard.Backquote
import web.keyboard.Backslash
import web.keyboard.Backspace
import web.keyboard.BracketLeft
import web.keyboard.BracketRight
import web.keyboard.BrowserBack
import web.keyboard.BrowserFavorites
import web.keyboard.BrowserForward
import web.keyboard.BrowserRefresh
import web.keyboard.CapsLock
import web.keyboard.Comma
import web.keyboard.ContextMenu
import web.keyboard.ControlLeft
import web.keyboard.ControlRight
import web.keyboard.Delete
import web.keyboard.Digit0
import web.keyboard.Digit1
import web.keyboard.Digit2
import web.keyboard.Digit3
import web.keyboard.Digit4
import web.keyboard.Digit5
import web.keyboard.Digit6
import web.keyboard.Digit7
import web.keyboard.Digit8
import web.keyboard.Digit9
import web.keyboard.End
import web.keyboard.Enter
import web.keyboard.Equal
import web.keyboard.Escape
import web.keyboard.F1
import web.keyboard.F10
import web.keyboard.F11
import web.keyboard.F12
import web.keyboard.F13
import web.keyboard.F14
import web.keyboard.F15
import web.keyboard.F2
import web.keyboard.F3
import web.keyboard.F4
import web.keyboard.F5
import web.keyboard.F6
import web.keyboard.F7
import web.keyboard.F8
import web.keyboard.F9
import web.keyboard.Help
import web.keyboard.Home
import web.keyboard.Insert
import web.keyboard.KeyA
import web.keyboard.KeyB
import web.keyboard.KeyC
import web.keyboard.KeyCode
import web.keyboard.KeyD
import web.keyboard.KeyE
import web.keyboard.KeyF
import web.keyboard.KeyG
import web.keyboard.KeyH
import web.keyboard.KeyI
import web.keyboard.KeyJ
import web.keyboard.KeyK
import web.keyboard.KeyL
import web.keyboard.KeyM
import web.keyboard.KeyN
import web.keyboard.KeyO
import web.keyboard.KeyP
import web.keyboard.KeyQ
import web.keyboard.KeyR
import web.keyboard.KeyS
import web.keyboard.KeyT
import web.keyboard.KeyU
import web.keyboard.KeyV
import web.keyboard.KeyW
import web.keyboard.KeyX
import web.keyboard.KeyY
import web.keyboard.KeyZ
import web.keyboard.LaunchApp1
import web.keyboard.LaunchMail
import web.keyboard.MediaPlayPause
import web.keyboard.MediaStop
import web.keyboard.MediaTrackNext
import web.keyboard.MediaTrackPrevious
import web.keyboard.MetaLeft
import web.keyboard.MetaRight
import web.keyboard.Minus
import web.keyboard.NumLock
import web.keyboard.Numpad0
import web.keyboard.Numpad1
import web.keyboard.Numpad2
import web.keyboard.Numpad3
import web.keyboard.Numpad4
import web.keyboard.Numpad5
import web.keyboard.Numpad6
import web.keyboard.Numpad7
import web.keyboard.Numpad8
import web.keyboard.Numpad9
import web.keyboard.NumpadAdd
import web.keyboard.NumpadComma
import web.keyboard.NumpadDecimal
import web.keyboard.NumpadDivide
import web.keyboard.NumpadEnter
import web.keyboard.NumpadEqual
import web.keyboard.NumpadMultiply
import web.keyboard.NumpadSubtract
import web.keyboard.PageDown
import web.keyboard.PageUp
import web.keyboard.Pause
import web.keyboard.Period
import web.keyboard.Power
import web.keyboard.Quote
import web.keyboard.ScrollLock
import web.keyboard.Semicolon
import web.keyboard.ShiftLeft
import web.keyboard.ShiftRight
import web.keyboard.Slash
import web.keyboard.Sleep
import web.keyboard.Space
import web.keyboard.Tab
import web.keyboard.WakeUp

internal val KEYS: BiMap<Key, KeyCode> = biMapOf(
    Key.Esc to KeyCode.Escape,
    Key.Number1 to KeyCode.Digit1,
    Key.Number2 to KeyCode.Digit2,
    Key.Number3 to KeyCode.Digit3,
    Key.Number4 to KeyCode.Digit4,
    Key.Number5 to KeyCode.Digit5,
    Key.Number6 to KeyCode.Digit6,
    Key.Number7 to KeyCode.Digit7,
    Key.Number8 to KeyCode.Digit8,
    Key.Number9 to KeyCode.Digit9,
    Key.Number0 to KeyCode.Digit0,
    Key.Minus to KeyCode.Minus,
    Key.Equal to KeyCode.Equal,
    Key.Backspace to KeyCode.Backspace,
    Key.Tab to KeyCode.Tab,
    Key.Q to KeyCode.KeyQ,
    Key.W to KeyCode.KeyW,
    Key.E to KeyCode.KeyE,
    Key.R to KeyCode.KeyR,
    Key.T to KeyCode.KeyT,
    Key.Y to KeyCode.KeyY,
    Key.U to KeyCode.KeyU,
    Key.I to KeyCode.KeyI,
    Key.O to KeyCode.KeyO,
    Key.P to KeyCode.KeyP,
    Key.LeftBrace to KeyCode.BracketLeft,
    Key.RightBrace to KeyCode.BracketRight,
    Key.Enter to KeyCode.Enter,
    Key.LeftCtrl to KeyCode.ControlLeft,
    Key.A to KeyCode.KeyA,
    Key.S to KeyCode.KeyS,
    Key.D to KeyCode.KeyD,
    Key.F to KeyCode.KeyF,
    Key.G to KeyCode.KeyG,
    Key.H to KeyCode.KeyH,
    Key.J to KeyCode.KeyJ,
    Key.K to KeyCode.KeyK,
    Key.L to KeyCode.KeyL,
    Key.Semicolon to KeyCode.Semicolon,
    Key.Apostrophe to KeyCode.Quote,
    Key.Backtick to KeyCode.Backquote,
    Key.LeftShift to KeyCode.ShiftLeft,
    Key.Backslash to KeyCode.Backslash,
    Key.Z to KeyCode.KeyZ,
    Key.X to KeyCode.KeyX,
    Key.C to KeyCode.KeyC,
    Key.V to KeyCode.KeyV,
    Key.B to KeyCode.KeyB,
    Key.N to KeyCode.KeyN,
    Key.M to KeyCode.KeyM,
    Key.Comma to KeyCode.Comma,
    Key.Dot to KeyCode.Period,
    Key.Slash to KeyCode.Slash,
    Key.RightShift to KeyCode.ShiftRight,
    Key.KeypadAsterisk to KeyCode.NumpadMultiply,
    Key.LeftAlt to KeyCode.AltLeft,
    Key.Space to KeyCode.Space,
    Key.CapsLock to KeyCode.CapsLock,
    Key.F1 to KeyCode.F1,
    Key.F2 to KeyCode.F2,
    Key.F3 to KeyCode.F3,
    Key.F4 to KeyCode.F4,
    Key.F5 to KeyCode.F5,
    Key.F6 to KeyCode.F6,
    Key.F7 to KeyCode.F7,
    Key.F8 to KeyCode.F8,
    Key.F9 to KeyCode.F9,
    Key.F10 to KeyCode.F10,
    Key.NumLock to KeyCode.NumLock,
    Key.ScrollLock to KeyCode.ScrollLock,
    Key.Keypad7 to KeyCode.Numpad7,
    Key.Keypad8 to KeyCode.Numpad8,
    Key.Keypad9 to KeyCode.Numpad9,
    Key.KeypadMinus to KeyCode.NumpadSubtract,
    Key.Keypad4 to KeyCode.Numpad4,
    Key.Keypad5 to KeyCode.Numpad5,
    Key.Keypad6 to KeyCode.Numpad6,
    Key.KeypadPlus to KeyCode.NumpadAdd,
    Key.Keypad1 to KeyCode.Numpad1,
    Key.Keypad2 to KeyCode.Numpad2,
    Key.Keypad3 to KeyCode.Numpad3,
    Key.Keypad0 to KeyCode.Numpad0,
    Key.KeypadDot to KeyCode.NumpadDecimal,
    Key.F11 to KeyCode.F11,
    Key.F12 to KeyCode.F12,
    Key.KeypadEnter to KeyCode.NumpadEnter,
    Key.RightCtrl to KeyCode.ControlRight,
    Key.KeypadSlash to KeyCode.NumpadDivide,
    Key.RightAlt to KeyCode.AltRight,
    Key.Home to KeyCode.Home,
    Key.Up to KeyCode.ArrowUp,
    Key.PageUp to KeyCode.PageUp,
    Key.Left to KeyCode.ArrowLeft,
    Key.Right to KeyCode.ArrowRight,
    Key.End to KeyCode.End,
    Key.Down to KeyCode.ArrowDown,
    Key.PageDown to KeyCode.PageDown,
    Key.Insert to KeyCode.Insert,
    Key.Delete to KeyCode.Delete,
    Key.Mute to KeyCode.AudioVolumeMute,
    Key.VolumeDown to KeyCode.AudioVolumeDown,
    Key.VolumeUp to KeyCode.AudioVolumeUp,
    Key.Power to KeyCode.Power,
    Key.KeypadEqual to KeyCode.NumpadEqual,
    Key.Pause to KeyCode.Pause,
    Key.KeypadComma to KeyCode.NumpadComma,
    Key.LeftSuper to KeyCode.MetaLeft,
    Key.RightSuper to KeyCode.MetaRight,
    Key.Compose to KeyCode.Compose,
    Key.Stop to KeyCode.Stop,
    Key.Again to KeyCode.Again,
    Key.Props to KeyCode.Props,
    Key.Undo to KeyCode.Undo,
    Key.Front to KeyCode.Front,
    Key.Copy to KeyCode.Copy,
    Key.Open to KeyCode.Open,
    Key.Paste to KeyCode.Paste,
    Key.Find to KeyCode.Find,
    Key.Cut to KeyCode.Cut,
    Key.Help to KeyCode.Help,
    Key.Menu to KeyCode.ContextMenu,
    Key.Calc to KeyCode.Calc,
    Key.Setup to KeyCode.Setup,
    Key.Sleep to KeyCode.Sleep,
    Key.Wakeup to KeyCode.WakeUp,
    Key.Mail to KeyCode.LaunchMail,
    Key.Bookmarks to KeyCode.BrowserFavorites,
    Key.Computer to KeyCode.Computer,
    Key.Back to KeyCode.BrowserBack,
    Key.Forward to KeyCode.BrowserForward,
    Key.NextSong to KeyCode.MediaTrackNext,
    Key.PlayPause to KeyCode.MediaPlayPause,
    Key.PreviousSong to KeyCode.MediaTrackPrevious,
    Key.StopCd to KeyCode.MediaStop,
    Key.Record to KeyCode.Record,
    Key.Rewind to KeyCode.Rewind,
    Key.Phone to KeyCode.LaunchApp1,
    Key.Refresh to KeyCode.BrowserRefresh,
    Key.F13 to KeyCode.F13,
    Key.F14 to KeyCode.F14,
    Key.F15 to KeyCode.F15,
    Key.F16 to KeyCode.F16,
    Key.F17 to KeyCode.F17,
    Key.F18 to KeyCode.F18,
    Key.F19 to KeyCode.F19,
    Key.F20 to KeyCode.F20,
    Key.F21 to KeyCode.F21,
    Key.F22 to KeyCode.F22,
    Key.F23 to KeyCode.F23,
    Key.F24 to KeyCode.F24,
)

