import ComposeApp
import Foundation

extension Kotlinx_datetimeLocalDate {
    func toDate() -> Date {
        var dateComponents = DateComponents()
        dateComponents.year = Int(self.year)
        dateComponents.month = Int(self.monthNumber)
        dateComponents.day = Int(self.dayOfMonth)
        
        let calendar = Calendar.current
        return calendar.date(from: dateComponents) ?? Date()
    }
}

extension Date {
    func toLocalDate() -> Kotlinx_datetimeLocalDate {
        let calendar = Calendar.current
        let components = calendar.dateComponents([.year, .month, .day], from: self)
        
        let year = Int32(components.year ?? 0)
        let month = Int32(components.month ?? 0)
        let day = Int32(components.day ?? 0)
        
        return Kotlinx_datetimeLocalDate(year: year, monthNumber: month, dayOfMonth: day)
    }
}
