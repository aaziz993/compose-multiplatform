import Foundation

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

print("Hello, World!")

let greeting = Greeting()
print(greeting.greet())

greeting.callBar()
greeting.callBazz()
