export function parseTimestamp(timestamp: number) {
  const date = new Date(timestamp);

  const year = date.getFullYear();
  const month = padZero(date.getMonth() + 1); // 月份从 0 开始
  const day = padZero(date.getDate());

  return {
    year,                  // 年，如 2025
    month,                 // 月，如 "07"
    day,                   // 日，如 "15"
    yearMonth: `${year}-${month}`,          // 年月：2025-07
    monthDay: `${month}-${day}`,            // 月日：07-15
    fullDate: `${year}-${month}-${day}`     // 年月日：2025-07-15
  };
}


function padZero(num: number): string {
  return num < 10 ? '0' + num : String(num);
}
