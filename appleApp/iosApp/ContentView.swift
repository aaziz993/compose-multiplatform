import SwiftUI

import SwiftUI

struct Greeting {
    func greet()->String {
        return "Hello!"
    }

    func callBar() {
        print("Calling Bar")
    }

    func callBazz() {
        print("Calling Bazz")
    }
}

struct ContentView: View {
    let greeting = Greeting()

    var body: some View {
        VStack(spacing: 24) {
            Text(greeting.greet())
                .font(.title)
            Button("Call Bar") {
                greeting.callBar()
            }
            Button("Call Bazz") {
                greeting.callBazz()
            }
        }
        .padding()
    }
}

#Preview {
    ContentView()
}
