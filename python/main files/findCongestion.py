def findCongestion( total_vehicles, heavy_vehicles, accident):
    report = []

    if total_vehicles >= 15 and (
            total_vehicles - heavy_vehicles) >= heavy_vehicles and heavy_vehicles <= 7 and accident != True:
        report.append('Recurrent Congestion')
        report.append('Usual Traffic')
        report.append('20m')
        report.append('10 Minutes')

        return report
    elif total_vehicles <= 30 and heavy_vehicles > 7 and accident != True:
        report.append('Non-Recurrent Congestion')
        report.append('Heavy Vehicles')
        report.append('30m')
        report.append('20 Minutes')
        return report
    elif total_vehicles <= 20 and heavy_vehicles >= 7 and accident == True:
        report.append('Non-Recurrent Congestion')
        report.append('Heavy Vehicles and Accident')
        report.append('25m')
        report.append('30 Minutes')
        return report
    elif (total_vehicles - heavy_vehicles) >= 10 and (total_vehicles - heavy_vehicles) <= 35 and accident == True:
        report.append('Non-Recurrent Congestion')
        report.append('Small Vehicles and Accident')
        report.append('30m')
        report.append('35 Minutes')
        return report
    elif (total_vehicles - heavy_vehicles) < 10 and heavy_vehicles < 7 and accident == True:
        report.append('No Congestion')
        report.append('None')
        report.append('10m')
        report.append('5 Minutes')
        return report



    else:
        report.append('No Congestion')
        report.append('None')
        report.append('10m')
        report.append('5 Minutes')
        return report

